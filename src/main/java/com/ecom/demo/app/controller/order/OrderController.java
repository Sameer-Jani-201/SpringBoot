package com.ecom.demo.app.controller.order;

import com.ecom.demo.app.dto.order.OrderResponse;
import com.ecom.demo.app.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("X-USER-ID") String userId) {
        Optional<OrderResponse> orderResponse = orderService.placeOrder(userId);
        return orderResponse.map(response -> new ResponseEntity<>(response, HttpStatus.CREATED)).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
