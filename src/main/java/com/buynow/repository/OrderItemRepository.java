package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}