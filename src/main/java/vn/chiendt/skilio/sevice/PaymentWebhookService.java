package vn.chiendt.skilio.sevice;

public interface PaymentWebhookService {
    
    /**
     * Handle Stripe webhook
     * @param payload Webhook payload
     * @param signature Stripe signature
     */
    void handleStripeWebhook(String payload, String signature);
    
    /**
     * Handle PayPal webhook
     * @param payload Webhook payload
     * @param transmissionId PayPal transmission ID
     */
    void handlePayPalWebhook(String payload, String transmissionId);
    
    /**
     * Handle generic payment webhook
     * @param payload Webhook payload
     * @param signature Webhook signature
     */
    void handleGenericWebhook(String payload, String signature);
    
    /**
     * Process payment success event
     * @param transactionId Transaction ID
     * @param gatewayProvider Gateway provider
     */
    void processPaymentSuccess(String transactionId, String gatewayProvider);
    
    /**
     * Process payment failure event
     * @param transactionId Transaction ID
     * @param gatewayProvider Gateway provider
     * @param errorMessage Error message
     */
    void processPaymentFailure(String transactionId, String gatewayProvider, String errorMessage);
    
    /**
     * Process refund event
     * @param transactionId Transaction ID
     * @param gatewayProvider Gateway provider
     * @param refundAmount Refund amount
     */
    void processRefund(String transactionId, String gatewayProvider, String refundAmount);
}
