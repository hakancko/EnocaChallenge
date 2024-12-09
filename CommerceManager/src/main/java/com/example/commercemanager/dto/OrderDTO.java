package com.example.commercemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderCode;
    private Long customerId;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalPrice;
    private OrderStatus status;
}
