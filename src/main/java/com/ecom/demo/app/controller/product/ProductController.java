package com.ecom.demo.app.controller.product;

import com.ecom.demo.app.dto.product.ProductRequest;
import com.ecom.demo.app.dto.product.ProductResponse;
import com.ecom.demo.app.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
        Optional<ProductResponse> response = productService.addProduct(productRequest);
        return response.map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.CREATED)).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProduct(){
        Optional<List<ProductResponse>> response = productService.fetchAll();
        if (response.isPresent() && response.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return response.map(products -> new ResponseEntity<>(products, HttpStatus.OK)).get();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request){
        Optional<ProductResponse> response = productService.updateProduct(id, request);
        return response.map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        Optional<ProductResponse> response = productService.getProductById(id);
        return response.map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<Boolean> isProductAvailable(@PathVariable Long id){
        Boolean available = productService.isProductAvailable(id);
        return ResponseEntity.ok(available);
    }
}
