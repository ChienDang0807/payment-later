package vn.chiendt.skilio.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.chiendt.skilio.constant.Currency;

import java.io.Serializable;


@Getter
@Setter
public class PaylaterPlanCreation implements Serializable {

    private Long userId;

    private String orderId;
    private String principalAmount;
    private Currency currency;
    private Integer installmentsTotal;
}
