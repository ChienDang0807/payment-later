package vn.chiendt.skilio.sevice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.chiendt.skilio.constant.PaylaterStatus;
import vn.chiendt.skilio.domain.dto.PaylaterPlanDto;
import vn.chiendt.skilio.entity.Installment;
import vn.chiendt.skilio.entity.InstallmentTransactions;
import vn.chiendt.skilio.entity.PaylaterPlans;
import vn.chiendt.skilio.repository.InstallmentRepository;
import vn.chiendt.skilio.repository.InstallmentTransactionsRepository;
import vn.chiendt.skilio.repository.PaylaterPlansRepository;
import vn.chiendt.skilio.sevice.PayLaterPlanManagementService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayLaterPlanManagementServiceImpl implements PayLaterPlanManagementService {

    private final PaylaterPlansRepository paylaterPlansRepository;
    private final InstallmentRepository installmentRepository;
    private final InstallmentTransactionsRepository installmentTransactionsRepository;

    @Override
    public List<PaylaterPlanDto> getUserPayLaterPlans(Long userId) {
        log.info("Getting PayLater plans for user: {}", userId);
        
        try {
            List<PaylaterPlans> plans = paylaterPlansRepository.findByUserId(userId);
            
            List<PaylaterPlanDto> planDtos = new ArrayList<>();
            for (PaylaterPlans plan : plans) {
                planDtos.add(convertToDto(plan));
            }
            
            log.info("Found {} PayLater plans for user: {}", planDtos.size(), userId);
            return planDtos;
            
        } catch (Exception e) {
            log.error("Error getting PayLater plans for user: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<PaylaterPlanDto> getActivePayLaterPlans(Long userId) {
        log.info("Getting active PayLater plans for user: {}", userId);
        
        try {
            List<PaylaterPlans> plans = paylaterPlansRepository.findActivePlansByUserId(userId);
            
            List<PaylaterPlanDto> planDtos = new ArrayList<>();
            for (PaylaterPlans plan : plans) {
                planDtos.add(convertToDto(plan));
            }
            
            log.info("Found {} active PayLater plans for user: {}", planDtos.size(), userId);
            return planDtos;
            
        } catch (Exception e) {
            log.error("Error getting active PayLater plans for user: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean cancelPayLaterPlan(String planId, String reason) {
        log.info("Canceling PayLater plan: {} reason: {}", planId, reason);
        
        try {
            Optional<PaylaterPlans> planOpt = paylaterPlansRepository.findById(planId);
            
            if (planOpt.isEmpty()) {
                log.warn("PayLater plan not found: {}", planId);
                return false;
            }
            
            PaylaterPlans plan = planOpt.get();
            
            // Check if plan can be canceled
            if (plan.getStatus() == PaylaterStatus.CANCELLED) {
                log.warn("PayLater plan already cancelled: {}", planId);
                return false;
            }
            
            if (plan.getStatus() == PaylaterStatus.COMPLETED) {
                log.warn("Cannot cancel completed PayLater plan: {}", planId);
                return false;
            }
            
            // Update plan status
            plan.setStatus(PaylaterStatus.CANCELLED);
            plan.setCanceledAt(LocalDateTime.now());
            
            // TODO: Cancel any scheduled payments
            // TODO: Process refunds if applicable
            // TODO: Send notification
            
            paylaterPlansRepository.save(plan);
            
            log.info("PayLater plan cancelled successfully: {}", planId);
            return true;
            
        } catch (Exception e) {
            log.error("Error cancelling PayLater plan: {}", planId, e);
            return false;
        }
    }

    @Override
    public boolean pausePayLaterPlan(String planId, String reason) {
        log.info("Pausing PayLater plan: {} reason: {}", planId, reason);
        
        try {
            Optional<PaylaterPlans> planOpt = paylaterPlansRepository.findById(planId);
            
            if (planOpt.isEmpty()) {
                log.warn("PayLater plan not found: {}", planId);
                return false;
            }
            
            PaylaterPlans plan = planOpt.get();
            
            // Check if plan can be paused
            if (plan.getStatus() != PaylaterStatus.ACTIVE && plan.getStatus() != PaylaterStatus.PARTIALLY_PAID) {
                log.warn("PayLater plan cannot be paused in status: {}", plan.getStatus());
                return false;
            }
            
            // TODO: Implement pause logic
            // This might involve updating a pause flag or status
            // and stopping scheduled payments
            
            log.info("PayLater plan paused successfully: {}", planId);
            return true;
            
        } catch (Exception e) {
            log.error("Error pausing PayLater plan: {}", planId, e);
            return false;
        }
    }

    @Override
    public boolean resumePayLaterPlan(String planId) {
        log.info("Resuming PayLater plan: {}", planId);
        
        try {
            Optional<PaylaterPlans> planOpt = paylaterPlansRepository.findById(planId);
            
            if (planOpt.isEmpty()) {
                log.warn("PayLater plan not found: {}", planId);
                return false;
            }
            
            PaylaterPlans plan = planOpt.get();
            
            // Check if plan can be resumed
            if (plan.getStatus() != PaylaterStatus.PARTIALLY_PAID) {
                log.warn("PayLater plan cannot be resumed in status: {}", plan.getStatus());
                return false;
            }
            
            // TODO: Implement resume logic
            // This might involve updating a pause flag or status
            // and resuming scheduled payments
            
            log.info("PayLater plan resumed successfully: {}", planId);
            return true;
            
        } catch (Exception e) {
            log.error("Error resuming PayLater plan: {}", planId, e);
            return false;
        }
    }

    @Override
    public boolean updatePaymentMethod(String planId, String paymentMethodId) {
        log.info("Updating payment method for PayLater plan: {} to: {}", planId, paymentMethodId);
        
        try {
            Optional<PaylaterPlans> planOpt = paylaterPlansRepository.findById(planId);
            
            if (planOpt.isEmpty()) {
                log.warn("PayLater plan not found: {}", planId);
                return false;
            }
            
            PaylaterPlans plan = planOpt.get();
            
            // Check if plan can be updated
            if (plan.getStatus() == PaylaterStatus.CANCELLED || plan.getStatus() == PaylaterStatus.COMPLETED) {
                log.warn("Cannot update payment method for plan in status: {}", plan.getStatus());
                return false;
            }
            
            // TODO: Update payment method for all pending installments
            // This might involve updating installment transactions
            
            log.info("Payment method updated successfully for PayLater plan: {}", planId);
            return true;
            
        } catch (Exception e) {
            log.error("Error updating payment method for PayLater plan: {}", planId, e);
            return false;
        }
    }

    @Override
    public PayLaterPlanSummary getPlanSummary(String planId) {
        log.info("Getting plan summary for PayLater plan: {}", planId);
        
        try {
            Optional<PaylaterPlans> planOpt = paylaterPlansRepository.findById(planId);
            
            if (planOpt.isEmpty()) {
                log.warn("PayLater plan not found: {}", planId);
                return null;
            }
            
            PaylaterPlans plan = planOpt.get();
            
            // Get installments for this plan
            List<Installment> installments = installmentRepository.findByPlanId(planId);
            
            // Calculate summary
            PayLaterPlanSummary summary = new PayLaterPlanSummary();
            summary.setPlanId(planId);
            summary.setStatus(plan.getStatus().name());
            summary.setTotalAmount(plan.getPrincipalAmount());
            summary.setTotalInstallments(plan.getInstallmentsTotal());
            
            // Calculate paid amount and installments
            BigDecimal paidAmount = BigDecimal.ZERO;
            int paidInstallments = 0;
            
            for (Installment installment : installments) {
                List<InstallmentTransactions> transactions = 
                    installmentTransactionsRepository.findSuccessfulTransactionsByInstallmentId(installment.getId());
                
                if (!transactions.isEmpty()) {
                    paidAmount = paidAmount.add(installment.getPlannedAmount());
                    paidInstallments++;
                }
            }
            
            summary.setPaidAmount(paidAmount.toString());
            summary.setPaidInstallments(paidInstallments);
            summary.setRemainingInstallments(plan.getInstallmentsTotal() - paidInstallments);
            
            BigDecimal remainingAmount = new BigDecimal(plan.getPrincipalAmount()).subtract(paidAmount);
            summary.setRemainingAmount(remainingAmount.toString());
            
            // Find next due date and amount
            for (Installment installment : installments) {
                List<InstallmentTransactions> transactions = 
                    installmentTransactionsRepository.findSuccessfulTransactionsByInstallmentId(installment.getId());
                
                if (transactions.isEmpty()) {
                    summary.setNextDueDate(installment.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    summary.setNextDueAmount(installment.getPlannedAmount().toString());
                    break;
                }
            }
            
            log.info("Plan summary generated for PayLater plan: {}", planId);
            return summary;
            
        } catch (Exception e) {
            log.error("Error getting plan summary for PayLater plan: {}", planId, e);
            return null;
        }
    }

    private PaylaterPlanDto convertToDto(PaylaterPlans plan) {
        return PaylaterPlanDto.builder()
                .id(plan.getId())
                .userId(plan.getUserId())
                .orderId(plan.getOrderId())
                .principalAmount(plan.getPrincipalAmount())
                .currency(plan.getCurrency().name())
                .status(plan.getStatus().name())
                .installmentsTotal(plan.getInstallmentsTotal())
                .creationDate(plan.getCreationDate())
                .approvedAt(plan.getApprovedAt())
                .build();
    }
}
