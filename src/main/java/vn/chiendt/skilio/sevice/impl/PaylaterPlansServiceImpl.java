package vn.chiendt.skilio.sevice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.chiendt.skilio.constant.Currency;
import vn.chiendt.skilio.constant.PaylaterStatus;
import vn.chiendt.skilio.constant.TransactionStatus;
import vn.chiendt.skilio.domain.dto.PaylaterPlanCreation;
import vn.chiendt.skilio.domain.dto.PaylaterPlanDto;
import vn.chiendt.skilio.entity.Installment;
import vn.chiendt.skilio.entity.InstallmentTransactions;
import vn.chiendt.skilio.entity.PaylaterPlans;
import vn.chiendt.skilio.repository.InstallmentRepository;
import vn.chiendt.skilio.sevice.PaylaterPlansService;
import vn.chiendt.skilio.sevice.feign.PaymentServiceFeignClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaylaterPlansServiceImpl implements PaylaterPlansService {

    private final InstallmentRepository installmentRepository;
    private final PaymentServiceFeignClient paymentService;

    @Override
    @Transactional
    public PaylaterPlanDto processPayLaterCheckout(String orderId, Long userId, BigDecimal totalAmount, String currency) {
        log.info("Processing PayLater checkout for orderId: {}, userId: {}, amount: {}", orderId, userId, totalAmount);
        
        try {
            // 1. Tạo PayLaterPlan
            PaylaterPlans plan = createPayLaterPlan(orderId, userId, totalAmount, currency);
            
            // 2. Sinh 3 installments
            List<Installment> installments = createInstallments(plan);
            
            // 3. Tạo transaction đầu tiên (installment #1)
            Installment firstInstallment = installments.get(0);
            InstallmentTransactions firstTransaction = createFirstTransaction(firstInstallment);
            
            // 4. Gọi PaymentService.charge()
            boolean paymentSuccess = processFirstPayment(firstTransaction, plan);
            
            if (paymentSuccess) {
                // 5a. Nếu thành công → mark PAID, lên lịch #2
                updateTransactionStatus(firstTransaction.getId(), TransactionStatus.SUCCESS);
                updatePlanStatus(plan.getId(), PaylaterStatus.ACTIVE);
                scheduleNextInstallment(installments.get(1));
                
                log.info("PayLater plan created successfully. PlanId: {}", plan.getId());
                return convertToDto(plan);
            } else {
                // 5b. Nếu fail → mark FAILED, trigger retry
                updateTransactionStatus(firstTransaction.getId(), TransactionStatus.FAILED);
                updatePlanStatus(plan.getId(), PaylaterStatus.PENDING);
                scheduleRetry(firstTransaction);
                
                log.warn("PayLater plan creation failed. PlanId: {}, will retry", plan.getId());
                return convertToDto(plan);
            }
            
        } catch (Exception e) {
            log.error("Error processing PayLater checkout for orderId: {}", orderId, e);
            throw new RuntimeException("Failed to process PayLater checkout", e);
        }
    }

    private PaylaterPlans createPayLaterPlan(String orderId, Long userId, BigDecimal totalAmount, String currency) {
        return PaylaterPlans.builder()
                .userId(userId)
                .orderId(orderId)
                .principalAmount(totalAmount)
                .currency(Currency.valueOf(currency))
                .status(PaylaterStatus.PENDING)
                .installmentsTotal(3)
                .creationDate(LocalDateTime.now())
                .build();
    }

    private List<Installment> createInstallments(PaylaterPlans plan) {
        List<Installment> installments = new ArrayList<>();
        BigDecimal principalAmount = new BigDecimal(plan.getPrincipalAmount());
        BigDecimal installmentAmount = principalAmount.divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
        
        for (int i = 1; i <= 3; i++) {
            LocalDateTime dueDate = LocalDateTime.now().plusMonths(i);
            
            Installment installment = Installment.builder()
                    .planId(plan.getId())
                    .installmentNumber(i)
                    .dueDate(dueDate)
                    .plannedAmount(installmentAmount)
                    .currency(plan.getCurrency().name())
                    .build();
            
            installments.add(installment);
        }
        
        // TODO: Save installments to database
        // installments = installmentRepository.saveAll(installments);
        
        return installments;
    }

    private InstallmentTransactions createFirstTransaction(Installment installment) {
        InstallmentTransactions transaction = InstallmentTransactions.builder()
                .installment(installment)
                .attemptNumber(1)
                .status(TransactionStatus.PENDING)
                .amount(installment.getPlannedAmount())
                .paymentMethodId("default_payment_method") // TODO: Get from user preferences
                .gatewayProvider("stripe") // TODO: Make configurable
                .build();
        
        // TODO: Save transaction to database
        // transaction = installmentTransactionsRepository.save(transaction);
        
        return transaction;
    }

    private boolean processFirstPayment(InstallmentTransactions transaction, PaylaterPlans plan) {
        try {
            // TODO: Call actual PaymentService.charge()
            // PaymentResult result = paymentService.charge(transaction.getAmount(), transaction.getPaymentMethodId());
            
            // For now, simulate payment processing
            boolean success = simulatePaymentProcessing();
            
            if (success) {
                transaction.setChargedAt(LocalDateTime.now());
                transaction.setPaymentRef("txn_" + UUID.randomUUID().toString());
                plan.setFirstChargeId(transaction.getPaymentRef());
                plan.setApprovedAt(LocalDateTime.now());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("Payment processing failed for transaction: {}", transaction.getId(), e);
            return false;
        }
    }

    private boolean simulatePaymentProcessing() {
        // TODO: Replace with actual payment service call
        // Simulate 90% success rate for demo
        return Math.random() > 0.1;
    }

    private void updateTransactionStatus(String transactionId, TransactionStatus status) {
        // TODO: Update transaction status in database
        log.info("Updating transaction {} status to {}", transactionId, status);
    }

    private void updatePlanStatus(String planId, PaylaterStatus status) {
        // TODO: Update plan status in database
        log.info("Updating plan {} status to {}", planId, status);
    }

    private void scheduleNextInstallment(Installment installment) {
        // TODO: Schedule next installment payment
        log.info("Scheduling next installment {} for plan {}", installment.getInstallmentNumber(), installment.getPlanId());
    }

    private void scheduleRetry(InstallmentTransactions transaction) {
        // TODO: Schedule retry mechanism
        log.info("Scheduling retry for transaction {}", transaction.getId());
    }

    @Override
    public PaylaterPlanDto createPaylaterPlan(PaylaterPlanCreation request) {
        // TODO: Implement based on requirements
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public PaylaterPlanDto getPaylaterPlan(String planId) {
        // TODO: Implement based on requirements
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updatePlanStatus(String planId, String status) {
        // TODO: Implement based on requirements
        throw new UnsupportedOperationException("Not implemented yet");
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
