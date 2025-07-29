package com.ecom.demo.app.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long userId;
    private BigDecimal totalPrice;
    private List<OrderItem> orderItems;
}
