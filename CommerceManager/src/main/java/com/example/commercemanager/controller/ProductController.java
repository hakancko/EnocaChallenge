package com.example.commercemanager.controller;

import com.example.commercemanager.dto.ProductDTO;
import com.example.commercemanager.entity.Product;
import com.example.commercemanager.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    // Manuel constructor
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        ProductDTO createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(modelMapper.map(createdProduct, ProductDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(modelMapper.map(product, ProductDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO
    ) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(modelMapper.map(updatedProduct, ProductDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}