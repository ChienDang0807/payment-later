package vn.chiendt.skilio.sevice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FeeCalculationService {
    
    /**
     * Calculate late fee for overdue payment
     * @param originalAmount Original payment amount
     * @param daysOverdue Days overdue
     * @return Late fee amount
     */
    BigDecimal calculateLateFee(BigDecimal originalAmount, int daysOverdue);
    
    /**
     * Calculate processing fee for payment
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @return Processing fee amount
     */
    BigDecimal calculateProcessingFee(BigDecimal amount, String paymentMethod);
    
    /**
     * Calculate interest for PayLater plan
     * @param principalAmount Principal amount
     * @param interestRate Interest rate (annual)
     * @param days Days to calculate interest for
     * @return Interest amount
     */
    BigDecimal calculateInterest(BigDecimal principalAmount, BigDecimal interestRate, int days);
    
    /**
     * Calculate total amount including fees
     * @param originalAmount Original amount
     * @param lateFee Late fee
     * @param processingFee Processing fee
     * @param interest Interest
     * @return Total amount
     */
    BigDecimal calculateTotalAmount(BigDecimal originalAmount, BigDecimal lateFee, 
                                  BigDecimal processingFee, BigDecimal interest);
    
    /**
     * Check if payment is overdue
     * @param dueDate Due date
     * @return True if overdue
     */
    boolean isPaymentOverdue(LocalDateTime dueDate);
    
    /**
     * Calculate days overdue
     * @param dueDate Due date
     * @return Days overdue
     */
    int calculateDaysOverdue(LocalDateTime dueDate);
}
