package com.example.commercemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
    public class CartItemDTO {
        private Long id;
        private ProductDTO product;
        private Integer quantity;
        private BigDecimal itemTotalPrice;
    }
