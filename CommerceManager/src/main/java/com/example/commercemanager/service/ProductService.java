package com.example.commercemanager.service;

import com.example.commercemanager.dto.ProductDTO;
import com.example.commercemanager.entity.Product;
import com.example.commercemanager.exception.ProductNotFoundException;
import com.example.commercemanager.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDTO createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new RuntimeException("Ürün zaten mevcut");
        }

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Ürün bulunamadı"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Transactional
    public void updateProductStock(Long productId, int quantityChange) {
        Product product = getProductById(productId);
        int newStock = product.getStockQuantity() + quantityChange;

        if (newStock < 0) {
            throw new RuntimeException("Stok miktarı negatife düşemez");
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);
    }

    public boolean checkProductAvailability(Long productId, int requestedQuantity) {
        Product product = getProductById(productId);
        return product.getStockQuantity() >= requestedQuantity;
    }
}