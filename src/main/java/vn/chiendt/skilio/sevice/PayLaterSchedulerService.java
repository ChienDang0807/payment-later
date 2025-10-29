package vn.chiendt.skilio.sevice;

import java.time.LocalDateTime;
import java.util.List;

public interface PayLaterSchedulerService {
    
    /**
     * Schedule next installment payment
     * @param installmentId Installment ID
     * @param dueDate Due date
     */
    void scheduleNextInstallment(String installmentId, LocalDateTime dueDate);
    
    /**
     * Schedule retry for failed transaction
     * @param transactionId Transaction ID
     * @param retryAfter Retry after this time
     */
    void scheduleRetry(String transactionId, LocalDateTime retryAfter);
    
    /**
     * Process scheduled payments
     */
    void processScheduledPayments();
    
    /**
     * Process retry payments
     */
    void processRetryPayments();
    
    /**
     * Cancel scheduled payment
     * @param installmentId Installment ID
     */
    void cancelScheduledPayment(String installmentId);
    
    /**
     * Get payments due today
     * @return List of installment IDs due today
     */
    List<String> getPaymentsDueToday();
    
    /**
     * Get overdue payments
     * @return List of overdue installment IDs
     */
    List<String> getOverduePayments();
}
