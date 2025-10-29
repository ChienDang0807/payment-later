package vn.chiendt.skilio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.chiendt.skilio.entity.PaylaterPlans;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaylaterPlansRepository extends JpaRepository<PaylaterPlans, String> {
    
    /**
     * Find PayLater plan by order ID
     * @param orderId Order ID
     * @return PayLater plan
     */
    Optional<PaylaterPlans> findByOrderId(String orderId);
    
    /**
     * Find PayLater plans by user ID
     * @param userId User ID
     * @return List of PayLater plans
     */
    List<PaylaterPlans> findByUserId(Long userId);
    
    /**
     * Find PayLater plans by status
     * @param status Status
     * @return List of PayLater plans
     */
    List<PaylaterPlans> findByStatus(String status);
    
    /**
     * Find PayLater plans by user ID and status
     * @param userId User ID
     * @param status Status
     * @return List of PayLater plans
     */
    List<PaylaterPlans> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * Find active PayLater plans for a user
     * @param userId User ID
     * @return List of active PayLater plans
     */
    @Query("SELECT p FROM PaylaterPlans p WHERE p.userId = :userId AND p.status IN ('ACTIVE', 'PARTIALLY_PAID')")
    List<PaylaterPlans> findActivePlansByUserId(@Param("userId") Long userId);
    
    /**
     * Count PayLater plans by user ID and status
     * @param userId User ID
     * @param status Status
     * @return Count
     */
    long countByUserIdAndStatus(Long userId, String status);
}
