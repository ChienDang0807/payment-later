package vn.chiendt.skilio.sevice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chiendt.skilio.constant.TransactionStatus;
import vn.chiendt.skilio.entity.Installment;
import vn.chiendt.skilio.entity.InstallmentTransactions;
import vn.chiendt.skilio.repository.InstallmentRepository;
import vn.chiendt.skilio.repository.InstallmentTransactionsRepository;
import vn.chiendt.skilio.sevice.PayLaterSchedulerService;
import vn.chiendt.skilio.sevice.PaymentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayLaterSchedulerServiceImpl implements PayLaterSchedulerService {

    private final InstallmentRepository installmentRepository;
    private final InstallmentTransactionsRepository installmentTransactionsRepository;
    private final PaymentService paymentService;

    @Override
    public void scheduleNextInstallment(String installmentId, LocalDateTime dueDate) {
        log.info("Scheduling next installment: {} for date: {}", installmentId, dueDate);
        
        // TODO: Implement actual scheduling mechanism
        // This could use Spring's @Scheduled, Quartz, or a message queue
        
        // For now, just log the scheduling
        log.info("Installment {} scheduled for {}", installmentId, dueDate);
    }

    @Override
    public void scheduleRetry(String transactionId, LocalDateTime retryAfter) {
        log.info("Scheduling retry for transaction: {} at {}", transactionId, retryAfter);
        
        // TODO: Implement actual retry scheduling mechanism
        // This could use Spring's @Scheduled, Quartz, or a message queue
        
        // For now, just log the retry scheduling
        log.info("Retry scheduled for transaction {} at {}", transactionId, retryAfter);
    }

    @Override
    @Scheduled(cron = "0 0 9 * * ?") // Run daily at 9 AM
    public void processScheduledPayments() {
        log.info("Processing scheduled payments...");
        
        try {
            List<String> dueToday = getPaymentsDueToday();
            log.info("Found {} payments due today", dueToday.size());
            
            for (String installmentId : dueToday) {
                processInstallmentPayment(installmentId);
            }
            
        } catch (Exception e) {
            log.error("Error processing scheduled payments", e);
        }
    }

    @Override
    @Scheduled(cron = "0 */30 * * * ?") // Run every 30 minutes
    public void processRetryPayments() {
        log.info("Processing retry payments...");
        
        try {
            List<InstallmentTransactions> retryTransactions = 
                installmentTransactionsRepository.findTransactionsNeedingRetry(3);
            
            log.info("Found {} transactions needing retry", retryTransactions.size());
            
            for (InstallmentTransactions transaction : retryTransactions) {
                processRetryTransaction(transaction);
            }
            
        } catch (Exception e) {
            log.error("Error processing retry payments", e);
        }
    }

    @Override
    public void cancelScheduledPayment(String installmentId) {
        log.info("Canceling scheduled payment for installment: {}", installmentId);
        
        // TODO: Implement actual cancellation mechanism
        // This could involve updating a scheduling table or canceling a scheduled job
        
        log.info("Scheduled payment canceled for installment: {}", installmentId);
    }

    @Override
    public List<String> getPaymentsDueToday() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        // TODO: Query database for installments due today
        // List<Installment> dueToday = installmentRepository.findByDueDateBetween(startOfDay, endOfDay);
        
        // For now, return empty list
        return new ArrayList<>();
    }

    @Override
    public List<String> getOverduePayments() {
        LocalDateTime now = LocalDateTime.now();
        
        // TODO: Query database for overdue installments
        // List<Installment> overdue = installmentRepository.findByDueDateBefore(now);
        
        // For now, return empty list
        return new ArrayList<>();
    }

    private void processInstallmentPayment(String installmentId) {
        log.info("Processing payment for installment: {}", installmentId);
        
        try {
            // TODO: Get installment from database
            // Installment installment = installmentRepository.findById(installmentId).orElse(null);
            // if (installment == null) {
            //     log.warn("Installment not found: {}", installmentId);
            //     return;
            // }
            
            // TODO: Create new transaction
            // InstallmentTransactions transaction = createTransaction(installment);
            
            // TODO: Process payment
            // PaymentService.PaymentResult result = paymentService.charge(
            //     transaction.getAmount(), 
            //     transaction.getPaymentMethodId(), 
            //     transaction.getInstallment().getCurrency()
            // );
            
            // TODO: Update transaction status based on result
            // if (result.isSuccess()) {
            //     updateTransactionStatus(transaction.getId(), TransactionStatus.SUCCESS);
            // } else {
            //     updateTransactionStatus(transaction.getId(), TransactionStatus.FAILED);
            //     scheduleRetry(transaction.getId(), LocalDateTime.now().plusHours(1));
            // }
            
            log.info("Payment processed for installment: {}", installmentId);
            
        } catch (Exception e) {
            log.error("Error processing payment for installment: {}", installmentId, e);
        }
    }

    private void processRetryTransaction(InstallmentTransactions transaction) {
        log.info("Processing retry for transaction: {}", transaction.getId());
        
        try {
            // Increment attempt number
            transaction.setAttemptNumber(transaction.getAttemptNumber() + 1);
            
            // Process payment
            PaymentService.PaymentResult result = paymentService.charge(
                transaction.getAmount(), 
                transaction.getPaymentMethodId(), 
                transaction.getInstallment().getCurrency()
            );
            
            if (result.isSuccess()) {
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setChargedAt(LocalDateTime.now());
                transaction.setPaymentRef(result.getTransactionId());
                log.info("Retry successful for transaction: {}", transaction.getId());
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setMessage(result.getErrorMessage());
                log.warn("Retry failed for transaction: {}", transaction.getId());
                
                // Schedule next retry if not exceeded max attempts
                if (transaction.getAttemptNumber() < 3) {
                    scheduleRetry(transaction.getId(), LocalDateTime.now().plusHours(2));
                }
            }
            
            // TODO: Save transaction to database
            // installmentTransactionsRepository.save(transaction);
            
        } catch (Exception e) {
            log.error("Error processing retry for transaction: {}", transaction.getId(), e);
        }
    }

    private InstallmentTransactions createTransaction(Installment installment) {
        return InstallmentTransactions.builder()
                .installment(installment)
                .attemptNumber(1)
                .status(TransactionStatus.PENDING)
                .amount(installment.getPlannedAmount())
                .paymentMethodId("default_payment_method") // TODO: Get from user preferences
                .gatewayProvider("stripe") // TODO: Make configurable
                .build();
    }

    private void updateTransactionStatus(String transactionId, TransactionStatus status) {
        // TODO: Update transaction status in database
        log.info("Updating transaction {} status to {}", transactionId, status);
    }
}
