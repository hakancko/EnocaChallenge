package com.example.commercemanager.repository;

import com.example.commercemanager.entity.Order;
import com.example.commercemanager.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderCode(String orderCode);
    List<Order> findByCustomerId(Long customerId);
}

