package com.ecom.demo.app.repository.order;

import com.ecom.demo.app.repository.product.Product;
import com.ecom.demo.app.repository.user.Address;
import com.ecom.demo.app.repository.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "order_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderedProduct> products = new ArrayList<>();
    private BigDecimal totalPrice;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
