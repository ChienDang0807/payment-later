package vn.chiendt.skilio.sevice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PayLaterReportingService {
    
    /**
     * Get PayLater plan statistics
     * @param startDate Start date
     * @param endDate End date
     * @return Plan statistics
     */
    PayLaterStatistics getPlanStatistics(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get user PayLater activity
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @return User activity
     */
    UserPayLaterActivity getUserActivity(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get payment success rate
     * @param startDate Start date
     * @param endDate End date
     * @return Payment success rate
     */
    PaymentSuccessRate getPaymentSuccessRate(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get overdue payments report
     * @param startDate Start date
     * @param endDate End date
     * @return Overdue payments report
     */
    OverduePaymentsReport getOverduePaymentsReport(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get revenue report
     * @param startDate Start date
     * @param endDate End date
     * @return Revenue report
     */
    RevenueReport getRevenueReport(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get plan status distribution
     * @return Plan status distribution
     */
    Map<String, Long> getPlanStatusDistribution();
    
    /**
     * Get installment status distribution
     * @return Installment status distribution
     */
    Map<String, Long> getInstallmentStatusDistribution();
    
    /**
     * PayLater statistics class
     */
    class PayLaterStatistics {
        private long totalPlans;
        private long activePlans;
        private long completedPlans;
        private long cancelledPlans;
        private BigDecimal totalRevenue;
        private BigDecimal totalOutstanding;
        private double averagePlanValue;
        private double completionRate;
        
        // Getters and setters
        public long getTotalPlans() { return totalPlans; }
        public void setTotalPlans(long totalPlans) { this.totalPlans = totalPlans; }
        
        public long getActivePlans() { return activePlans; }
        public void setActivePlans(long activePlans) { this.activePlans = activePlans; }
        
        public long getCompletedPlans() { return completedPlans; }
        public void setCompletedPlans(long completedPlans) { this.completedPlans = completedPlans; }
        
        public long getCancelledPlans() { return cancelledPlans; }
        public void setCancelledPlans(long cancelledPlans) { this.cancelledPlans = cancelledPlans; }
        
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
        
        public BigDecimal getTotalOutstanding() { return totalOutstanding; }
        public void setTotalOutstanding(BigDecimal totalOutstanding) { this.totalOutstanding = totalOutstanding; }
        
        public double getAveragePlanValue() { return averagePlanValue; }
        public void setAveragePlanValue(double averagePlanValue) { this.averagePlanValue = averagePlanValue; }
        
        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    }
    
    /**
     * User PayLater activity class
     */
    class UserPayLaterActivity {
        private Long userId;
        private long totalPlans;
        private long activePlans;
        private long completedPlans;
        private BigDecimal totalSpent;
        private BigDecimal totalOutstanding;
        private double averagePlanValue;
        private int totalInstallments;
        private int paidInstallments;
        private int overdueInstallments;
        
        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public long getTotalPlans() { return totalPlans; }
        public void setTotalPlans(long totalPlans) { this.totalPlans = totalPlans; }
        
        public long getActivePlans() { return activePlans; }
        public void setActivePlans(long activePlans) { this.activePlans = activePlans; }
        
        public long getCompletedPlans() { return completedPlans; }
        public void setCompletedPlans(long completedPlans) { this.completedPlans = completedPlans; }
        
        public BigDecimal getTotalSpent() { return totalSpent; }
        public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
        
        public BigDecimal getTotalOutstanding() { return totalOutstanding; }
        public void setTotalOutstanding(BigDecimal totalOutstanding) { this.totalOutstanding = totalOutstanding; }
        
        public double getAveragePlanValue() { return averagePlanValue; }
        public void setAveragePlanValue(double averagePlanValue) { this.averagePlanValue = averagePlanValue; }
        
        public int getTotalInstallments() { return totalInstallments; }
        public void setTotalInstallments(int totalInstallments) { this.totalInstallments = totalInstallments; }
        
        public int getPaidInstallments() { return paidInstallments; }
        public void setPaidInstallments(int paidInstallments) { this.paidInstallments = paidInstallments; }
        
        public int getOverdueInstallments() { return overdueInstallments; }
        public void setOverdueInstallments(int overdueInstallments) { this.overdueInstallments = overdueInstallments; }
    }
    
    /**
     * Payment success rate class
     */
    class PaymentSuccessRate {
        private long totalAttempts;
        private long successfulAttempts;
        private long failedAttempts;
        private double successRate;
        private double failureRate;
        
        // Getters and setters
        public long getTotalAttempts() { return totalAttempts; }
        public void setTotalAttempts(long totalAttempts) { this.totalAttempts = totalAttempts; }
        
        public long getSuccessfulAttempts() { return successfulAttempts; }
        public void setSuccessfulAttempts(long successfulAttempts) { this.successfulAttempts = successfulAttempts; }
        
        public long getFailedAttempts() { return failedAttempts; }
        public void setFailedAttempts(long failedAttempts) { this.failedAttempts = failedAttempts; }
        
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        
        public double getFailureRate() { return failureRate; }
        public void setFailureRate(double failureRate) { this.failureRate = failureRate; }
    }
    
    /**
     * Overdue payments report class
     */
    class OverduePaymentsReport {
        private long totalOverdueInstallments;
        private BigDecimal totalOverdueAmount;
        private long overduePlans;
        private double averageDaysOverdue;
        private List<OverdueInstallment> overdueInstallments;
        
        // Getters and setters
        public long getTotalOverdueInstallments() { return totalOverdueInstallments; }
        public void setTotalOverdueInstallments(long totalOverdueInstallments) { this.totalOverdueInstallments = totalOverdueInstallments; }
        
        public BigDecimal getTotalOverdueAmount() { return totalOverdueAmount; }
        public void setTotalOverdueAmount(BigDecimal totalOverdueAmount) { this.totalOverdueAmount = totalOverdueAmount; }
        
        public long getOverduePlans() { return overduePlans; }
        public void setOverduePlans(long overduePlans) { this.overduePlans = overduePlans; }
        
        public double getAverageDaysOverdue() { return averageDaysOverdue; }
        public void setAverageDaysOverdue(double averageDaysOverdue) { this.averageDaysOverdue = averageDaysOverdue; }
        
        public List<OverdueInstallment> getOverdueInstallments() { return overdueInstallments; }
        public void setOverdueInstallments(List<OverdueInstallment> overdueInstallments) { this.overdueInstallments = overdueInstallments; }
    }
    
    /**
     * Overdue installment class
     */
    class OverdueInstallment {
        private String planId;
        private String installmentId;
        private Long userId;
        private BigDecimal amount;
        private int daysOverdue;
        private LocalDateTime dueDate;
        
        // Getters and setters
        public String getPlanId() { return planId; }
        public void setPlanId(String planId) { this.planId = planId; }
        
        public String getInstallmentId() { return installmentId; }
        public void setInstallmentId(String installmentId) { this.installmentId = installmentId; }
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        
        public int getDaysOverdue() { return daysOverdue; }
        public void setDaysOverdue(int daysOverdue) { this.daysOverdue = daysOverdue; }
        
        public LocalDateTime getDueDate() { return dueDate; }
        public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    }
    
    /**
     * Revenue report class
     */
    class RevenueReport {
        private BigDecimal totalRevenue;
        private BigDecimal totalFees;
        private BigDecimal totalRefunds;
        private BigDecimal netRevenue;
        private Map<String, BigDecimal> revenueByMonth;
        private Map<String, BigDecimal> revenueByStatus;
        
        // Getters and setters
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
        
        public BigDecimal getTotalFees() { return totalFees; }
        public void setTotalFees(BigDecimal totalFees) { this.totalFees = totalFees; }
        
        public BigDecimal getTotalRefunds() { return totalRefunds; }
        public void setTotalRefunds(BigDecimal totalRefunds) { this.totalRefunds = totalRefunds; }
        
        public BigDecimal getNetRevenue() { return netRevenue; }
        public void setNetRevenue(BigDecimal netRevenue) { this.netRevenue = netRevenue; }
        
        public Map<String, BigDecimal> getRevenueByMonth() { return revenueByMonth; }
        public void setRevenueByMonth(Map<String, BigDecimal> revenueByMonth) { this.revenueByMonth = revenueByMonth; }
        
        public Map<String, BigDecimal> getRevenueByStatus() { return revenueByStatus; }
        public void setRevenueByStatus(Map<String, BigDecimal> revenueByStatus) { this.revenueByStatus = revenueByStatus; }
    }
}
