package vn.chiendt.skilio.sevice;

import vn.chiendt.skilio.domain.dto.PaylaterPlanDto;

import java.util.List;

public interface PayLaterPlanManagementService {
    
    /**
     * Get all PayLater plans for a user
     * @param userId User ID
     * @return List of PayLater plans
     */
    List<PaylaterPlanDto> getUserPayLaterPlans(Long userId);
    
    /**
     * Get active PayLater plans for a user
     * @param userId User ID
     * @return List of active PayLater plans
     */
    List<PaylaterPlanDto> getActivePayLaterPlans(Long userId);
    
    /**
     * Cancel PayLater plan
     * @param planId Plan ID
     * @param reason Cancellation reason
     * @return True if cancellation was successful
     */
    boolean cancelPayLaterPlan(String planId, String reason);
    
    /**
     * Pause PayLater plan
     * @param planId Plan ID
     * @param reason Pause reason
     * @return True if pause was successful
     */
    boolean pausePayLaterPlan(String planId, String reason);
    
    /**
     * Resume PayLater plan
     * @param planId Plan ID
     * @return True if resume was successful
     */
    boolean resumePayLaterPlan(String planId);
    
    /**
     * Update payment method for PayLater plan
     * @param planId Plan ID
     * @param paymentMethodId New payment method ID
     * @return True if update was successful
     */
    boolean updatePaymentMethod(String planId, String paymentMethodId);
    
    /**
     * Get PayLater plan summary
     * @param planId Plan ID
     * @return Plan summary
     */
    PayLaterPlanSummary getPlanSummary(String planId);
    
    /**
     * PayLater plan summary class
     */
    class PayLaterPlanSummary {
        private String planId;
        private String status;
        private String totalAmount;
        private String paidAmount;
        private String remainingAmount;
        private int totalInstallments;
        private int paidInstallments;
        private int remainingInstallments;
        private String nextDueDate;
        private String nextDueAmount;
        
        // Getters and setters
        public String getPlanId() { return planId; }
        public void setPlanId(String planId) { this.planId = planId; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getTotalAmount() { return totalAmount; }
        public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }
        
        public String getPaidAmount() { return paidAmount; }
        public void setPaidAmount(String paidAmount) { this.paidAmount = paidAmount; }
        
        public String getRemainingAmount() { return remainingAmount; }
        public void setRemainingAmount(String remainingAmount) { this.remainingAmount = remainingAmount; }
        
        public int getTotalInstallments() { return totalInstallments; }
        public void setTotalInstallments(int totalInstallments) { this.totalInstallments = totalInstallments; }
        
        public int getPaidInstallments() { return paidInstallments; }
        public void setPaidInstallments(int paidInstallments) { this.paidInstallments = paidInstallments; }
        
        public int getRemainingInstallments() { return remainingInstallments; }
        public void setRemainingInstallments(int remainingInstallments) { this.remainingInstallments = remainingInstallments; }
        
        public String getNextDueDate() { return nextDueDate; }
        public void setNextDueDate(String nextDueDate) { this.nextDueDate = nextDueDate; }
        
        public String getNextDueAmount() { return nextDueAmount; }
        public void setNextDueAmount(String nextDueAmount) { this.nextDueAmount = nextDueAmount; }
    }
}
