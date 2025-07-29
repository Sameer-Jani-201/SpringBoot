package com.ecom.demo.app.service.product;

import com.ecom.demo.app.dto.product.ProductRequest;
import com.ecom.demo.app.dto.product.ProductResponse;
import com.ecom.demo.app.repository.product.Product;
import com.ecom.demo.app.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<ProductResponse> addProduct(ProductRequest productRequest) {
        Product product = mapToProduct(productRequest);
        product = productRepository.save(product);
        ProductResponse response = mapToProductResponse(product);
        return Optional.of(response);
    }

    public Optional<List<ProductResponse>> fetchAll() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response = products.stream().map(this::mapToProductResponse).toList();
        return Optional.of(response);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest request) {
        return productRepository.findById(id).stream().findFirst().map(product -> {
            product.setQuantity(request.getQuantity());
            product.setProductPrice(request.getProductPrice());
            product.setProductName(request.getProductName());
            product = productRepository.save(product);
            ProductResponse response = mapToProductResponse(product);
            return Optional.of(response);
        }).orElse(Optional.empty());
    }

    public Optional<ProductResponse> getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.isPresent() ? product.map(this::mapToProductResponse) : Optional.empty();
    }

    public Boolean isProductAvailable(Long id) {
        Optional<ProductResponse> response = getProductById(id);
        return response.filter(productResponse -> productResponse.getQuantity() > 0).isPresent();
    }

    private Product mapToProduct(ProductRequest productRequest){
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setProductPrice(productRequest.getProductPrice());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        return product;
    }

    private ProductResponse mapToProductResponse(Product product){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setProductName(product.getProductName());
        productResponse.setDescription(product.getDescription());
        productResponse.setProductPrice(product.getProductPrice());
        productResponse.setQuantity(product.getQuantity());
        return productResponse;
    }
}
