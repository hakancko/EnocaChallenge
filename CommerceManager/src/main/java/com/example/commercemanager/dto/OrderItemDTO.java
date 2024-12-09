package com.example.commercemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal itemPrice;
    private BigDecimal itemTotalPrice;
}
