package vn.chiendt.skilio.domain.dto;

import lombok.*;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaylaterPlanDto {

    private String id;
    private Long userId;
    private String orderId;
    private BigDecimal principalAmount;
    private String currency;
    private String status;
    private Integer installmentsTotal;

    private LocalDateTime creationDate;
    private LocalDateTime approvedAt;
}
