package com.example.commercemanager.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
}

enum OrderStatus {
    PENDING, COMPLETED, CANCELLED
}