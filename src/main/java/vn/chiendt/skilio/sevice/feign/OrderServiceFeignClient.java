package vn.chiendt.skilio.sevice.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service", url = "${url.order-service}")
public interface OrderServiceFeignClient {
}
