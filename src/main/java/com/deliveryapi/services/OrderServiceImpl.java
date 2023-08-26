package com.deliveryapi.services;

import com.deliveryapi.exceptions.ResourceNotFoundException;
import com.deliveryapi.models.Order;
import com.deliveryapi.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found"));
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        Order.OrderStatus orderStatus = Order.OrderStatus.fromValue(status);
        return orderRepository.findByStatus(orderStatus);
    }

    @Override
    public void createOrder(@Valid Order order) {
        orderRepository.save(order);
    }

    @Override
    public ResponseEntity<Void> updateOrder(Long id, @Valid Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            orderRepository.save(order);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
    }

}
