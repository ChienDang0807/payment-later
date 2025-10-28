package vn.chiendt.skilio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fee_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 100)
    private String name; // "Default Late Fee", "Service Fee"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FeeType type; // LATE_FEE, SERVICE_FEE,...

    @Column(name = "fixed_amount", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal fixedAmount = BigDecimal.ZERO;

    @Column(name = "percent_amount", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal percentAmount = BigDecimal.ZERO;

    @Column(name = "min_amount", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal minAmount = BigDecimal.ZERO;

    @Column(name = "max_amount", precision = 12, scale = 2)
    private BigDecimal maxAmount;

    @Column(length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(name = "applies_to", length = 50)
    private String appliesTo; // USER, PLAN, INSTALLMENT

    @Column(name = "condition_expression", columnDefinition = "TEXT")
    private String conditionExpression; // JSON logic hoặc SpEL (vd: "overdueDays > 0")

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "active")
    @Builder.Default
    private Boolean active = true;

    @Column(name = "effective_from")
    @Builder.Default
    private LocalDateTime effectiveFrom = LocalDateTime.now();

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
