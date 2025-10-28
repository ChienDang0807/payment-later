package vn.chiendt.skilio.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import vn.chiendt.skilio.constant.Currency;
import vn.chiendt.skilio.constant.PaylaterStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "paylater_plans")
public class PaylaterPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "first_charge_id")
    private String firstChargeId;

    @Column(name = "pricipal_amount")
    private String principalAmount; // tổng tiền ban đầu

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Currency currency;

    @Column(name = "installments_total")
    @Builder.Default
    private int installmentsTotal = 3;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PaylaterStatus status;

    @Column(name = "creation_date")
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
}
