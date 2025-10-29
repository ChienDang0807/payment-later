package vn.chiendt.skilio.sevice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.chiendt.skilio.constant.TransactionStatus;
import vn.chiendt.skilio.entity.InstallmentTransactions;
import vn.chiendt.skilio.repository.InstallmentTransactionsRepository;
import vn.chiendt.skilio.sevice.PaymentService;
import vn.chiendt.skilio.sevice.RetryService;
import vn.chiendt.skilio.sevice.feign.PaymentServiceFeignClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryServiceImpl implements RetryService {

    private final InstallmentTransactionsRepository installmentTransactionsRepository;
    private final PaymentServiceFeignClient paymentService;

    @Override
    public void scheduleRetry(String transactionId, LocalDateTime retryAfter, int maxRetries) {
        log.info("Scheduling retry for transaction: {} at {} (max retries: {})", 
                transactionId, retryAfter, maxRetries);
        
        try {
            // TODO: Implement actual retry scheduling mechanism
            // This could use Spring's @Scheduled, Quartz, or a message queue
            
            // For now, just log the retry scheduling
            log.info("Retry scheduled for transaction: {} at {}", transactionId, retryAfter);
            
        } catch (Exception e) {
            log.error("Error scheduling retry for transaction: {}", transactionId, e);
        }
    }

    @Override
    public boolean processRetry(String transactionId) {
        log.info("Processing retry for transaction: {}", transactionId);
        
        try {
            Optional<InstallmentTransactions> transactionOpt = 
                installmentTransactionsRepository.findById(transactionId);
            
            if (transactionOpt.isEmpty()) {
                log.warn("Transaction not found for retry: {}", transactionId);
                return false;
            }
            
            InstallmentTransactions transaction = transactionOpt.get();
            
            // Check if transaction can be retried
            if (!canRetry(transactionId)) {
                log.warn("Transaction {} cannot be retried", transactionId);
                return false;
            }
            
            // Increment retry count
            transaction.setAttemptNumber(transaction.getAttemptNumber() + 1);
            
            // Process payment
            PaymentService.PaymentResult result = paymentService.charge(
                transaction.getAmount(), 
                transaction.getPaymentMethodId(), 
                transaction.getInstallment().getCurrency()
            );
            
            if (result.isSuccess()) {
                // Update transaction status
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setChargedAt(LocalDateTime.now());
                transaction.setPaymentRef(result.getTransactionId());
                transaction.setMessage("Payment successful via retry");
                
                log.info("Retry successful for transaction: {}", transactionId);
                
            } else {
                // Update transaction status
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setMessage("Payment failed via retry: " + result.getErrorMessage());
                
                log.warn("Retry failed for transaction: {}", transactionId);
            }
            
            // Save transaction
            installmentTransactionsRepository.save(transaction);
            
            return result.isSuccess();
            
        } catch (Exception e) {
            log.error("Error processing retry for transaction: {}", transactionId, e);
            return false;
        }
    }

    @Override
    public List<String> getTransactionsNeedingRetry() {
        log.debug("Getting transactions that need retry");
        
        try {
            List<InstallmentTransactions> retryTransactions = 
                installmentTransactionsRepository.findTransactionsNeedingRetry(3);
            
            List<String> transactionIds = new ArrayList<>();
            for (InstallmentTransactions transaction : retryTransactions) {
                if (canRetry(transaction.getId())) {
                    transactionIds.add(transaction.getId());
                }
            }
            
            log.debug("Found {} transactions needing retry", transactionIds.size());
            return transactionIds;
            
        } catch (Exception e) {
            log.error("Error getting transactions needing retry", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void cancelRetry(String transactionId) {
        log.info("Canceling retry for transaction: {}", transactionId);
        
        try {
            // TODO: Implement actual retry cancellation mechanism
            // This could involve updating a scheduling table or canceling a scheduled job
            
            log.info("Retry canceled for transaction: {}", transactionId);
            
        } catch (Exception e) {
            log.error("Error canceling retry for transaction: {}", transactionId, e);
        }
    }

    @Override
    public int getRetryCount(String transactionId) {
        log.debug("Getting retry count for transaction: {}", transactionId);
        
        try {
            Optional<InstallmentTransactions> transactionOpt = 
                installmentTransactionsRepository.findById(transactionId);
            
            if (transactionOpt.isEmpty()) {
                log.warn("Transaction not found: {}", transactionId);
                return 0;
            }
            
            InstallmentTransactions transaction = transactionOpt.get();
            return transaction.getAttemptNumber() - 1; // Subtract 1 because attemptNumber includes the first attempt
            
        } catch (Exception e) {
            log.error("Error getting retry count for transaction: {}", transactionId, e);
            return 0;
        }
    }

    @Override
    public boolean canRetry(String transactionId) {
        log.debug("Checking if transaction can be retried: {}", transactionId);
        
        try {
            Optional<InstallmentTransactions> transactionOpt = 
                installmentTransactionsRepository.findById(transactionId);
            
            if (transactionOpt.isEmpty()) {
                log.warn("Transaction not found: {}", transactionId);
                return false;
            }
            
            InstallmentTransactions transaction = transactionOpt.get();
            
            // Check if transaction is in failed status
            if (transaction.getStatus() != TransactionStatus.FAILED) {
                log.debug("Transaction {} is not in failed status: {}", transactionId, transaction.getStatus());
                return false;
            }
            
            // Check if retry count is within limits
            int retryCount = getRetryCount(transactionId);
            if (retryCount >= 3) { // Max 3 retries
                log.debug("Transaction {} has exceeded max retry count: {}", transactionId, retryCount);
                return false;
            }
            
            // Check if enough time has passed since last attempt
            LocalDateTime lastAttempt = transaction.getUpdatedAt();
            LocalDateTime now = LocalDateTime.now();
            long hoursSinceLastAttempt = java.time.Duration.between(lastAttempt, now).toHours();
            
            if (hoursSinceLastAttempt < 1) { // Wait at least 1 hour between retries
                log.debug("Transaction {} retry too soon: {} hours since last attempt", 
                        transactionId, hoursSinceLastAttempt);
                return false;
            }
            
            log.debug("Transaction {} can be retried", transactionId);
            return true;
            
        } catch (Exception e) {
            log.error("Error checking if transaction can be retried: {}", transactionId, e);
            return false;
        }
    }

    @Override
    public LocalDateTime getNextRetryTime(String transactionId) {
        log.debug("Getting next retry time for transaction: {}", transactionId);
        
        try {
            Optional<InstallmentTransactions> transactionOpt = 
                installmentTransactionsRepository.findById(transactionId);
            
            if (transactionOpt.isEmpty()) {
                log.warn("Transaction not found: {}", transactionId);
                return null;
            }
            
            InstallmentTransactions transaction = transactionOpt.get();
            
            // Calculate next retry time based on retry count
            int retryCount = getRetryCount(transactionId);
            LocalDateTime lastAttempt = transaction.getUpdatedAt();
            
            // Exponential backoff: 1 hour, 2 hours, 4 hours
            int hoursToWait = (int) Math.pow(2, retryCount);
            LocalDateTime nextRetryTime = lastAttempt.plusHours(hoursToWait);
            
            log.debug("Next retry time for transaction {}: {}", transactionId, nextRetryTime);
            return nextRetryTime;
            
        } catch (Exception e) {
            log.error("Error getting next retry time for transaction: {}", transactionId, e);
            return null;
        }
    }
}
