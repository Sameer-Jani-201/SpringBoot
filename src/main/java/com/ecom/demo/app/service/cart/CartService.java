package com.ecom.demo.app.service.cart;

import com.ecom.demo.app.dto.cart.CartItemResponse;
import com.ecom.demo.app.dto.cart.CartRequest;
import com.ecom.demo.app.dto.cart.CartResponse;
import com.ecom.demo.app.repository.cart.Cart;
import com.ecom.demo.app.repository.cart.CartItem;
import com.ecom.demo.app.repository.cart.CartRepository;
import com.ecom.demo.app.repository.product.Product;
import com.ecom.demo.app.repository.product.ProductRepository;
import com.ecom.demo.app.repository.user.User;
import com.ecom.demo.app.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Optional<CartResponse> addCart(String userId, CartRequest request) {
        Optional<User> user = userRepository.findById(Long.valueOf(userId));
        if (user.isEmpty()) {
            return Optional.empty();
        }
        Optional<Product> product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            return Optional.empty();
        }
        Optional<Cart> existingCart = cartRepository.findByUserId(user.get().getId());
        if (existingCart.isPresent()) {
            List<CartItem> cartItems = existingCart.get().getCartItems();
            boolean isNotAdded = true;
            for (CartItem cI : cartItems) {
                if (Objects.equals(cI.getProductId(), request.getProductId())) {
                    isNotAdded = false;
                    cI.setQuantity(cI.getQuantity() + request.getQuantity());
                    cI.setCart(existingCart.get());
                    if (cI.getQuantity() <= 0) {
                        cartItems.remove(cI);
                    }
                    break;
                }
            }
            if (isNotAdded && request.getQuantity() > 0) {
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(request.getQuantity());
                cartItem.setProductId(request.getProductId());
                cartItem.setPrice(product.get().getProductPrice());
                cartItem.setCart(existingCart.get());
                existingCart.get().getCartItems().add(cartItem);
            }
            if (!existingCart.get().getCartItems().isEmpty()) {
                cartRepository.save(existingCart.get());
            } else {
                cartRepository.save(existingCart.get());
                deleteCartByUserId(existingCart.get());
            }
            // Here, update with existing cart response
            return fetchCartByUserId(Long.valueOf(userId));
        }
        Cart cart = new Cart();
        cart.setUserId(Long.valueOf(userId));
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(request.getQuantity());
        cartItem.setCart(cart);
        cartItem.setProductId(request.getProductId());
        cartItem.setPrice(product.get().getProductPrice());
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
        return fetchCartByUserId(Long.valueOf(userId));
    }

    public Optional<CartResponse> fetchCartByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            return Optional.empty();
        }
        Optional<Cart> cart = cartRepository.findByUserId(user.get().getId());
        if (cart.isEmpty()) {
            return Optional.empty();
        }
        CartResponse cartResponse = new CartResponse();
        cartResponse.setUserId(cart.get().getUserId());
        List<CartItemResponse> cartItems = new ArrayList<>();
        for (CartItem cartItem : cart.get().getCartItems()) {
            CartItemResponse cartItemResponse = new CartItemResponse();
            Optional<Product> product = productRepository.findById(cartItem.getProductId());
            if (product.isPresent()) {
                cartItemResponse.setDescription(product.get().getDescription());
                cartItemResponse.setProductPrice(product.get().getProductPrice());
                cartItemResponse.setProductName(product.get().getProductName());
                cartItemResponse.setQuantity(cartItem.getQuantity());
                cartItems.add(cartItemResponse);
            }
        }
        cartResponse.setCartItems(cartItems);
        return Optional.of(cartResponse);
    }

    public void deleteCartByUserId(Cart cart){
        cartRepository.delete(cart);
    }
}
