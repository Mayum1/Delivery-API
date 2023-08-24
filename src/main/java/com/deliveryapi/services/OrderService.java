package com.deliveryapi.services;

import com.deliveryapi.models.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();
    Order getOrderById(Long id);
    List<Order> getOrdersByStatus(String status);
    void createOrder(Order order);
    boolean updateOrder(Long id, Order order);
    boolean deleteOrder(Long id);

}
