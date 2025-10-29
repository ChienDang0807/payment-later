package vn.chiendt.skilio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.chiendt.skilio.domain.dto.PaylaterPlanDto;
import vn.chiendt.skilio.sevice.PaylaterPlansService;

@Slf4j
@RestController
@RequestMapping("/api/paylater")
@RequiredArgsConstructor
public class PayLaterController {

    private final PaylaterPlansService paylaterPlansService;

    /**
     * Process PayLater checkout after order creation
     * POST /api/paylater/checkout
     */
    @PostMapping("/checkout")
    public ResponseEntity<PaylaterPlanDto> processPayLaterCheckout(
            @RequestParam String orderId,
            @RequestParam Long userId,
            @RequestParam String totalAmount,
            @RequestParam String currency) {
        
        log.info("Processing PayLater checkout for orderId: {}, userId: {}", orderId, userId);
        
        try {
            PaylaterPlanDto plan = paylaterPlansService.processPayLaterCheckout(
                orderId, userId, totalAmount, currency);
            
            return ResponseEntity.ok(plan);
            
        } catch (Exception e) {
            log.error("Error processing PayLater checkout", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get PayLater plan details
     * GET /api/paylater/plans/{planId}
     */
    @GetMapping("/plans/{planId}")
    public ResponseEntity<PaylaterPlanDto> getPayLaterPlan(@PathVariable String planId) {
        log.info("Getting PayLater plan: {}", planId);
        
        try {
            PaylaterPlanDto plan = paylaterPlansService.getPaylaterPlan(planId);
            
            if (plan != null) {
                return ResponseEntity.ok(plan);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("Error getting PayLater plan: {}", planId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Update PayLater plan status
     * PUT /api/paylater/plans/{planId}/status
     */
    @PutMapping("/plans/{planId}/status")
    public ResponseEntity<Void> updatePlanStatus(
            @PathVariable String planId,
            @RequestParam String status) {
        
        log.info("Updating PayLater plan {} status to {}", planId, status);
        
        try {
            paylaterPlansService.updatePlanStatus(planId, status);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            log.error("Error updating PayLater plan status: {}", planId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Health check endpoint
     * GET /api/paylater/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("PayLater service is running");
    }
}
