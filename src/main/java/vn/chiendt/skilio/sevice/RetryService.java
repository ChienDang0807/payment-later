package vn.chiendt.skilio.sevice;

import java.time.LocalDateTime;
import java.util.List;

public interface RetryService {
    
    /**
     * Schedule retry for failed transaction
     * @param transactionId Transaction ID
     * @param retryAfter Retry after this time
     * @param maxRetries Maximum number of retries
     */
    void scheduleRetry(String transactionId, LocalDateTime retryAfter, int maxRetries);
    
    /**
     * Process retry for failed transaction
     * @param transactionId Transaction ID
     * @return True if retry was successful
     */
    boolean processRetry(String transactionId);
    
    /**
     * Get transactions that need retry
     * @return List of transaction IDs that need retry
     */
    List<String> getTransactionsNeedingRetry();
    
    /**
     * Cancel retry for transaction
     * @param transactionId Transaction ID
     */
    void cancelRetry(String transactionId);
    
    /**
     * Get retry count for transaction
     * @param transactionId Transaction ID
     * @return Number of retries attempted
     */
    int getRetryCount(String transactionId);
    
    /**
     * Check if transaction can be retried
     * @param transactionId Transaction ID
     * @return True if transaction can be retried
     */
    boolean canRetry(String transactionId);
    
    /**
     * Get next retry time for transaction
     * @param transactionId Transaction ID
     * @return Next retry time
     */
    LocalDateTime getNextRetryTime(String transactionId);
}
