package com.eshoppingzone.order.client;

import com.eshoppingzone.order.dto.DeliveryCreateRequest;
import com.eshoppingzone.order.dto.DeliveryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service", fallback = DeliveryClientFallback.class)
public interface DeliveryClient {

    @PostMapping("/api/v1/delivery")
    DeliveryDto createDelivery(@RequestBody DeliveryCreateRequest request);
}
