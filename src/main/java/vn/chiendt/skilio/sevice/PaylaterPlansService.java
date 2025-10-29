package vn.chiendt.skilio.sevice;

import vn.chiendt.skilio.domain.dto.PaylaterPlanCreation;
import vn.chiendt.skilio.domain.dto.PaylaterPlanDto;
import vn.chiendt.skilio.entity.PaylaterPlans;

import java.math.BigDecimal;

public interface PaylaterPlansService {

    /**
     *  Create paylaterPlan
     * @param request
     * @return
     */
    PaylaterPlanDto createPaylaterPlan(PaylaterPlanCreation request);
    
    /**
     * Process PayLater plan after checkout order
     * @param orderId
     * @param userId
     * @param totalAmount
     * @param currency
     * @return
     */
    PaylaterPlanDto processPayLaterCheckout(String orderId, Long userId, BigDecimal totalAmount, String currency);
    
    /**
     * Get PayLater plan by ID
     * @param planId
     * @return
     */
    PaylaterPlanDto getPaylaterPlan(String planId);
    
    /**
     * Update PayLater plan status
     * @param planId
     * @param status
     */
    void updatePlanStatus(String planId, String status);
}
