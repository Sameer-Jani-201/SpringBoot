package com.ecom.demo.app.controller.cart;

import com.ecom.demo.app.dto.cart.CartRequest;
import com.ecom.demo.app.dto.cart.CartResponse;
import com.ecom.demo.app.dto.user.UserResponse;
import com.ecom.demo.app.service.cart.CartService;
import com.ecom.demo.app.service.product.ProductService;
import com.ecom.demo.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addCart(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody CartRequest request){
        Optional<CartResponse> cartResponse = cartService.addCart(userId, request);
        return cartResponse.map(response -> new ResponseEntity<>(response, HttpStatus.CREATED)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public ResponseEntity<CartResponse> getProductByUserId(@RequestHeader("X-USER-ID") String userId){
        Optional<CartResponse> cartResponse = cartService.fetchCartByUserId(Long.valueOf(userId));
        return cartResponse.map(response -> new ResponseEntity<>(response, HttpStatus.OK)).orElseGet(() -> ResponseEntity.noContent().build());
    }

}
