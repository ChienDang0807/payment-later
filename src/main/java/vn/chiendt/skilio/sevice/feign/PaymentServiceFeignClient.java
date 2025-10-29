package vn.chiendt.skilio.sevice.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment-service", url = "${url.payment-service}")
public interface PaymentServiceFeignClient {
}
