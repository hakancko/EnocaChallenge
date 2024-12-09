package com.example.commercemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private CustomerDTO customer;
    private List<CartItemDTO> cartItems;
    private BigDecimal totalPrice;
}
