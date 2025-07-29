package com.ecom.demo.app.service.order;

import com.ecom.demo.app.dto.cart.CartResponse;
import com.ecom.demo.app.dto.order.OrderItem;
import com.ecom.demo.app.dto.order.OrderResponse;
import com.ecom.demo.app.repository.cart.Cart;
import com.ecom.demo.app.repository.cart.CartItem;
import com.ecom.demo.app.repository.cart.CartRepository;
import com.ecom.demo.app.repository.order.Order;
import com.ecom.demo.app.repository.order.OrderRepository;
import com.ecom.demo.app.repository.order.OrderedProduct;
import com.ecom.demo.app.repository.product.Product;
import com.ecom.demo.app.repository.product.ProductRepository;
import com.ecom.demo.app.repository.user.User;
import com.ecom.demo.app.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Optional<OrderResponse> placeOrder(String userId) {
        Optional<User> user = userRepository.findById(Long.valueOf(userId));
        if (user.isEmpty()) {
            return Optional.empty();
        }
        Optional<Cart> cart = cartRepository.findByUserId(Long.valueOf(userId));
        if (cart.isEmpty()) {
            return Optional.empty();
        }
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        List<OrderedProduct> orderItems = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        for (CartItem cartItem : cart.get().getCartItems()) {
            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setQuantity(cartItem.getQuantity());
            orderedProduct.setProductPrice(cartItem.getPrice());
            Optional<Product> product = productRepository.findById(cartItem.getProductId());
            product.get().setQuantity(product.get().getQuantity() - cartItem.getQuantity());
            orderedProduct.setProductName(product.get().getProductName());
            orderedProduct.setOrder(order);
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity().longValue());
            total = total.add(cartItem.getPrice().multiply(quantity));
            productRepository.save(product.get());
            orderItems.add(orderedProduct);
        }
        order.setTotalPrice(total);
        order.setProducts(orderItems);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setUserId(Long.valueOf(userId));
        orderResponse.setTotalPrice(total);
        orderResponse.setOrderItems(orderItems.stream().map(orderedProduct -> new OrderItem(orderedProduct.getProductName(), orderedProduct.getProductPrice(), orderedProduct.getQuantity())).toList());
        orderRepository.save(order);
        cartRepository.delete(cart.get());
        return Optional.of(orderResponse);
    }
}
