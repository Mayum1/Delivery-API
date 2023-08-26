package com.deliveryapi.services;

import com.deliveryapi.models.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    List<Order> getOrdersByStatus(String status);

    void createOrder(Order order);

    ResponseEntity<Void> updateOrder(Long id, Order order);

    ResponseEntity<Void> deleteOrder(Long id);

}
