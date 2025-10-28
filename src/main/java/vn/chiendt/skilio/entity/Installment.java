package vn.chiendt.skilio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "installment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Installment extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber; // 1..3

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "planned_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal plannedAmount; // số tiền dự kiến phải trả

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Một installment có thể có nhiều transaction (retry, refund,...)
    @OneToMany(mappedBy = "installment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InstallmentTransactions> transactions;
}
