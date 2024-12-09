package com.example.commercemanager.service;

import com.example.commercemanager.dto.OrderDTO;
import com.example.commercemanager.dto.OrderItemDTO;
import com.example.commercemanager.entity.*;
import com.example.commercemanager.exception.EmptyCartException;
import com.example.commercemanager.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, CustomerService customerService, CartService cartService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public OrderDTO placeOrder(Long customerId, OrderDTO orderDTO) {
        Customer customer = customerService.getCustomerById(customerId);
        Cart cart = customer.getCart();

        if (cart.getCartItems().isEmpty()) {
            throw new EmptyCartException("Sepet boş olduğundan sipariş oluşturulamıyor");
        }

        Order order = new Order();
        order.setOrderCode(generateUniqueOrderCode());
        order.setCustomer(customer);
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setItemPrice(cartItem.getProduct().getPrice());
                    orderItem.setItemTotalPrice(cartItem.getItemTotalPrice());
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.emptyCart(customerId);

        savedOrder.setStatus(OrderStatus.COMPLETED);
        savedOrder = orderRepository.save(savedOrder);

        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    public OrderDTO getOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode);
        return modelMapper.map(order, OrderDTO.class);
    }

    public List<OrderDTO> getAllOrdersForCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    private String generateUniqueOrderCode() {
        return UUID.randomUUID().toString();
    }
}