package com.ecom.demo.app.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private String productName;
    private String description;
    private BigDecimal productPrice;
    private Integer quantity;
}
