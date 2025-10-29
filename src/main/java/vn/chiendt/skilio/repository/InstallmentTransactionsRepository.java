package vn.chiendt.skilio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.chiendt.skilio.entity.InstallmentTransactions;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentTransactionsRepository extends JpaRepository<InstallmentTransactions, String> {
    
    /**
     * Find transactions by installment ID
     * @param installmentId Installment ID
     * @return List of transactions
     */
    List<InstallmentTransactions> findByInstallmentId(String installmentId);
    
    /**
     * Find transactions by installment ID and status
     * @param installmentId Installment ID
     * @param status Status
     * @return List of transactions
     */
    List<InstallmentTransactions> findByInstallmentIdAndStatus(String installmentId, String status);
    
    /**
     * Find the latest transaction for an installment
     * @param installmentId Installment ID
     * @return Latest transaction
     */
    @Query("SELECT t FROM InstallmentTransactions t WHERE t.installment.id = :installmentId ORDER BY t.attemptNumber DESC")
    Optional<InstallmentTransactions> findLatestTransactionByInstallmentId(@Param("installmentId") String installmentId);
    
    /**
     * Find successful transactions by installment ID
     * @param installmentId Installment ID
     * @return List of successful transactions
     */
    @Query("SELECT t FROM InstallmentTransactions t WHERE t.installment.id = :installmentId AND t.status = 'SUCCESS'")
    List<InstallmentTransactions> findSuccessfulTransactionsByInstallmentId(@Param("installmentId") String installmentId);
    
    /**
     * Find failed transactions by installment ID
     * @param installmentId Installment ID
     * @return List of failed transactions
     */
    @Query("SELECT t FROM InstallmentTransactions t WHERE t.installment.id = :installmentId AND t.status = 'FAILED'")
    List<InstallmentTransactions> findFailedTransactionsByInstallmentId(@Param("installmentId") String installmentId);
    
    /**
     * Find transactions by payment reference
     * @param paymentRef Payment reference
     * @return Transaction
     */
    Optional<InstallmentTransactions> findByPaymentRef(String paymentRef);
    
    /**
     * Find transactions by gateway provider and payment reference
     * @param gatewayProvider Gateway provider
     * @param paymentRef Payment reference
     * @return Transaction
     */
    Optional<InstallmentTransactions> findByGatewayProviderAndPaymentRef(String gatewayProvider, String paymentRef);
    
    /**
     * Count transactions by installment ID and status
     * @param installmentId Installment ID
     * @param status Status
     * @return Count
     */
    long countByInstallmentIdAndStatus(String installmentId, String status);
    
    /**
     * Find transactions that need retry (failed and not too many attempts)
     * @param maxAttempts Maximum number of attempts
     * @return List of transactions that need retry
     */
    @Query("SELECT t FROM InstallmentTransactions t WHERE t.status = 'FAILED' AND t.attemptNumber < :maxAttempts")
    List<InstallmentTransactions> findTransactionsNeedingRetry(@Param("maxAttempts") int maxAttempts);
}
