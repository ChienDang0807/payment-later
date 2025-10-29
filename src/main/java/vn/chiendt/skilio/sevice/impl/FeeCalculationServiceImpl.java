package vn.chiendt.skilio.sevice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.chiendt.skilio.sevice.FeeCalculationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeeCalculationServiceImpl implements FeeCalculationService {

    // Configuration constants - these should come from application properties
    private static final BigDecimal LATE_FEE_RATE = new BigDecimal("0.05"); // 5% per month
    private static final BigDecimal PROCESSING_FEE_RATE = new BigDecimal("0.029"); // 2.9%
    private static final BigDecimal MIN_PROCESSING_FEE = new BigDecimal("0.30"); // $0.30
    private static final BigDecimal MAX_PROCESSING_FEE = new BigDecimal("10.00"); // $10.00
    private static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("0.12"); // 12% annual

    @Override
    public BigDecimal calculateLateFee(BigDecimal originalAmount, int daysOverdue) {
        log.debug("Calculating late fee for amount: {} days overdue: {}", originalAmount, daysOverdue);
        
        if (daysOverdue <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Calculate late fee as percentage of original amount
        // Late fee increases with days overdue
        BigDecimal lateFeeRate = LATE_FEE_RATE.multiply(new BigDecimal(daysOverdue))
                                             .divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP); // Monthly rate
        
        BigDecimal lateFee = originalAmount.multiply(lateFeeRate);
        
        // Cap late fee at 50% of original amount
        BigDecimal maxLateFee = originalAmount.multiply(new BigDecimal("0.50"));
        if (lateFee.compareTo(maxLateFee) > 0) {
            lateFee = maxLateFee;
        }
        
        log.debug("Late fee calculated: {}", lateFee);
        return lateFee.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateProcessingFee(BigDecimal amount, String paymentMethod) {
        log.debug("Calculating processing fee for amount: {} method: {}", amount, paymentMethod);
        
        BigDecimal processingFee;
        
        switch (paymentMethod.toLowerCase()) {
            case "credit_card":
            case "debit_card":
                // 2.9% + $0.30 for cards
                processingFee = amount.multiply(PROCESSING_FEE_RATE).add(MIN_PROCESSING_FEE);
                break;
            case "bank_transfer":
            case "ach":
                // 1% for bank transfers
                processingFee = amount.multiply(new BigDecimal("0.01"));
                break;
            case "paypal":
                // 2.9% + $0.30 for PayPal
                processingFee = amount.multiply(PROCESSING_FEE_RATE).add(MIN_PROCESSING_FEE);
                break;
            default:
                // Default to card rate
                processingFee = amount.multiply(PROCESSING_FEE_RATE).add(MIN_PROCESSING_FEE);
        }
        
        // Apply min/max limits
        if (processingFee.compareTo(MIN_PROCESSING_FEE) < 0) {
            processingFee = MIN_PROCESSING_FEE;
        } else if (processingFee.compareTo(MAX_PROCESSING_FEE) > 0) {
            processingFee = MAX_PROCESSING_FEE;
        }
        
        log.debug("Processing fee calculated: {}", processingFee);
        return processingFee.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateInterest(BigDecimal principalAmount, BigDecimal interestRate, int days) {
        log.debug("Calculating interest for principal: {} rate: {} days: {}", 
                principalAmount, interestRate, days);
        
        if (days <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Calculate daily interest rate
        BigDecimal dailyRate = interestRate.divide(new BigDecimal("365"), 6, RoundingMode.HALF_UP);
        
        // Calculate interest: principal * daily_rate * days
        BigDecimal interest = principalAmount.multiply(dailyRate).multiply(new BigDecimal(days));
        
        log.debug("Interest calculated: {}", interest);
        return interest.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal originalAmount, BigDecimal lateFee, 
                                         BigDecimal processingFee, BigDecimal interest) {
        log.debug("Calculating total amount for original: {} lateFee: {} processingFee: {} interest: {}", 
                originalAmount, lateFee, processingFee, interest);
        
        BigDecimal total = originalAmount.add(lateFee).add(processingFee).add(interest);
        
        log.debug("Total amount calculated: {}", total);
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean isPaymentOverdue(LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        return dueDate.isBefore(now);
    }

    @Override
    public int calculateDaysOverdue(LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        
        if (!isPaymentOverdue(dueDate)) {
            return 0;
        }
        
        long days = ChronoUnit.DAYS.between(dueDate, now);
        return (int) Math.max(0, days);
    }
}
