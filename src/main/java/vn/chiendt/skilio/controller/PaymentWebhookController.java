package vn.chiendt.skilio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.chiendt.skilio.sevice.PaymentWebhookService;

@Slf4j
@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentWebhookService paymentWebhookService;

    /**
     * Handle payment webhook from gateway
     * POST /api/payments/webhook/stripe
     */
    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        
        log.info("Received Stripe webhook");
        
        try {
            paymentWebhookService.handleStripeWebhook(payload, signature);
            return ResponseEntity.ok("Webhook processed successfully");
            
        } catch (Exception e) {
            log.error("Error processing Stripe webhook", e);
            return ResponseEntity.badRequest().body("Webhook processing failed");
        }
    }

    /**
     * Handle payment webhook from gateway
     * POST /api/payments/webhook/paypal
     */
    @PostMapping("/paypal")
    public ResponseEntity<String> handlePayPalWebhook(
            @RequestBody String payload,
            @RequestHeader("PayPal-Transmission-Id") String transmissionId) {
        
        log.info("Received PayPal webhook");
        
        try {
            paymentWebhookService.handlePayPalWebhook(payload, transmissionId);
            return ResponseEntity.ok("Webhook processed successfully");
            
        } catch (Exception e) {
            log.error("Error processing PayPal webhook", e);
            return ResponseEntity.badRequest().body("Webhook processing failed");
        }
    }

    /**
     * Handle generic payment webhook
     * POST /api/payments/webhook/generic
     */
    @PostMapping("/generic")
    public ResponseEntity<String> handleGenericWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Webhook-Signature") String signature) {
        
        log.info("Received generic payment webhook");
        
        try {
            paymentWebhookService.handleGenericWebhook(payload, signature);
            return ResponseEntity.ok("Webhook processed successfully");
            
        } catch (Exception e) {
            log.error("Error processing generic webhook", e);
            return ResponseEntity.badRequest().body("Webhook processing failed");
        }
    }
}
