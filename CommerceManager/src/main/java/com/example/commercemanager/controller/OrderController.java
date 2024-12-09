package com.example.commercemanager.controller;

import com.example.commercemanager.dto.OrderDTO;
import com.example.commercemanager.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable Long customerId, @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.placeOrder(customerId, orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<OrderDTO> getOrderByCode(@PathVariable String orderCode) {
        OrderDTO orderDTO = orderService.getOrderByCode(orderCode);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersForCustomer(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.getAllOrdersForCustomer(customerId);
        return ResponseEntity.ok(orders);
    }
}