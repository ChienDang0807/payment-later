package vn.chiendt.skilio.sevice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.chiendt.skilio.constant.TransactionStatus;
import vn.chiendt.skilio.entity.InstallmentTransactions;
import vn.chiendt.skilio.repository.InstallmentTransactionsRepository;
import vn.chiendt.skilio.sevice.PaymentWebhookService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookServiceImpl implements PaymentWebhookService {

    private final InstallmentTransactionsRepository installmentTransactionsRepository;

    @Override
    public void handleStripeWebhook(String payload, String signature) {
        log.info("Processing Stripe webhook with signature: {}", signature);
        
        try {
            // TODO: Verify Stripe signature
            // StripeWebhook.constructEvent(payload, signature, webhookSecret);
            
            // TODO: Parse Stripe webhook event
            // Event event = StripeWebhook.constructEvent(payload, signature, webhookSecret);
            
            // For now, simulate webhook processing
            log.info("Stripe webhook processed successfully");
            
        } catch (Exception e) {
            log.error("Error processing Stripe webhook", e);
            throw new RuntimeException("Failed to process Stripe webhook", e);
        }
    }

    @Override
    public void handlePayPalWebhook(String payload, String transmissionId) {
        log.info("Processing PayPal webhook with transmission ID: {}", transmissionId);
        
        try {
            // TODO: Verify PayPal webhook signature
            // PayPalWebhook.verifyWebhook(payload, transmissionId, webhookSecret);
            
            // TODO: Parse PayPal webhook event
            // PayPalWebhookEvent event = PayPalWebhook.parseEvent(payload);
            
            // For now, simulate webhook processing
            log.info("PayPal webhook processed successfully");
            
        } catch (Exception e) {
            log.error("Error processing PayPal webhook", e);
            throw new RuntimeException("Failed to process PayPal webhook", e);
        }
    }

    @Override
    public void handleGenericWebhook(String payload, String signature) {
        log.info("Processing generic payment webhook with signature: {}", signature);
        
        try {
            // TODO: Verify generic webhook signature
            // GenericWebhook.verifySignature(payload, signature, webhookSecret);
            
            // TODO: Parse generic webhook event
            // GenericWebhookEvent event = GenericWebhook.parseEvent(payload);
            
            // For now, simulate webhook processing
            log.info("Generic payment webhook processed successfully");
            
        } catch (Exception e) {
            log.error("Error processing generic webhook", e);
            throw new RuntimeException("Failed to process generic webhook", e);
        }
    }

    @Override
    public void processPaymentSuccess(String transactionId, String gatewayProvider) {
        log.info("Processing payment success for transaction: {} from gateway: {}", 
                transactionId, gatewayProvider);
        
        try {
            // Find transaction by payment reference
            InstallmentTransactions transaction = installmentTransactionsRepository
                .findByGatewayProviderAndPaymentRef(gatewayProvider, transactionId)
                .orElse(null);
            
            if (transaction == null) {
                log.warn("Transaction not found for payment reference: {}", transactionId);
                return;
            }
            
            // Update transaction status
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setChargedAt(LocalDateTime.now());
            transaction.setMessage("Payment successful via webhook");
            
            // Save transaction
            installmentTransactionsRepository.save(transaction);
            
            log.info("Payment success processed for transaction: {}", transaction.getId());
            
        } catch (Exception e) {
            log.error("Error processing payment success for transaction: {}", transactionId, e);
        }
    }

    @Override
    public void processPaymentFailure(String transactionId, String gatewayProvider, String errorMessage) {
        log.info("Processing payment failure for transaction: {} from gateway: {}", 
                transactionId, gatewayProvider);
        
        try {
            // Find transaction by payment reference
            InstallmentTransactions transaction = installmentTransactionsRepository
                .findByGatewayProviderAndPaymentRef(gatewayProvider, transactionId)
                .orElse(null);
            
            if (transaction == null) {
                log.warn("Transaction not found for payment reference: {}", transactionId);
                return;
            }
            
            // Update transaction status
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setMessage("Payment failed via webhook: " + errorMessage);
            
            // Save transaction
            installmentTransactionsRepository.save(transaction);
            
            log.info("Payment failure processed for transaction: {}", transaction.getId());
            
        } catch (Exception e) {
            log.error("Error processing payment failure for transaction: {}", transactionId, e);
        }
    }

    @Override
    public void processRefund(String transactionId, String gatewayProvider, String refundAmount) {
        log.info("Processing refund for transaction: {} from gateway: {} amount: {}", 
                transactionId, gatewayProvider, refundAmount);
        
        try {
            // Find transaction by payment reference
            InstallmentTransactions transaction = installmentTransactionsRepository
                .findByGatewayProviderAndPaymentRef(gatewayProvider, transactionId)
                .orElse(null);
            
            if (transaction == null) {
                log.warn("Transaction not found for payment reference: {}", transactionId);
                return;
            }
            
            // Update transaction status
            transaction.setStatus(TransactionStatus.REFUNDED);
            transaction.setRefundAmount(new java.math.BigDecimal(refundAmount));
            transaction.setMessage("Refund processed via webhook");
            
            // Save transaction
            installmentTransactionsRepository.save(transaction);
            
            log.info("Refund processed for transaction: {}", transaction.getId());
            
        } catch (Exception e) {
            log.error("Error processing refund for transaction: {}", transactionId, e);
        }
    }
}
