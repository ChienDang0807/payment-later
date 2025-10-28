package vn.chiendt.skilio.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.chiendt.skilio.constant.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "installment_tranctions")
public class InstallmentTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installment_id", nullable = false)
    private Installment installment; // FK → installments(id)

    @Column(name = "attempt_number")
    @Builder.Default
    private Integer attemptNumber = 1; // Lần thanh toán thứ mấy

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransactionStatus status; // PENDING, SUCCESS, FAILED, REFUNDED

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount; // Số tiền thanh toán thực tế

    @Column(name = "charged_at")
    private LocalDateTime chargedAt; // Thời điểm charge thành công

    @Column(name = "late_fee", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal lateFee = BigDecimal.ZERO;

    @Column(name = "refund_amount", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal refundAmount = BigDecimal.ZERO;

    @Column(name = "payment_method_id")
    private String paymentMethodId;

    @Column(name = "payment_ref", length = 100)
    private String paymentRef; // Mã giao dịch từ cổng thanh toán

    @Column(name = "gateway_provider", length = 50)
    private String gatewayProvider; // Stripe, Momo,...

    @Column(name = "message", columnDefinition = "TEXT")
    private String message; // Mô tả lỗi / ghi chú

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; // JSON lưu response, merchantRef,...

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();


}
