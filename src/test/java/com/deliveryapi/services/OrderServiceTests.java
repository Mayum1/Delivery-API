package com.deliveryapi.services;

import com.deliveryapi.exceptions.ResourceNotFoundException;
import com.deliveryapi.models.Order;
import com.deliveryapi.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTests {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllOrders() {
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order());
        mockOrders.add(new Order());

        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<Order> orders = orderService.getAllOrders();

        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        Order order = orderService.getOrderById(orderId);

        assertEquals(orderId, order.getId());
    }

    @Test
    void testGetOrderByStatus() {
        String status = "обработка";
        Order.OrderStatus orderStatus = Order.OrderStatus.fromValue(status);

        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order());
        mockOrders.add(new Order());

        when(orderRepository.findByStatus(orderStatus)).thenReturn(mockOrders);

        List<Order> orders = orderService.getOrdersByStatus(status);

        assertEquals(2, orders.size());
    }

    @Test
    void testCreateOrder() {
        Order mockOrder = new Order();

        orderService.createOrder(mockOrder);

        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testUpdateOrder() {
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);

        when(orderRepository.existsById(orderId)).thenReturn(true);

        ResponseEntity<Void> response = orderService.updateOrder(orderId, mockOrder);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testUpdateOrder_notFound() {
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);

        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(orderId, mockOrder));

        verify(orderRepository, never()).save(mockOrder);
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(true);

        ResponseEntity<Void> response = orderService.deleteOrder(orderId);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void testDeleteOrder_notFound() {
        Long orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(orderId));

        verify(orderRepository, never()).deleteById(orderId);
    }
}
