package com.nskay.microservices.product.controller;

import com.nskay.microservices.product.dto.ProductRequest;
import com.nskay.microservices.product.dto.ProductResponse;
import com.nskay.microservices.product.model.Product;
import com.nskay.microservices.product.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    public final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
}
