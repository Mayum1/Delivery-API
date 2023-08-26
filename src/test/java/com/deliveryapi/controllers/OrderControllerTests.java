package com.deliveryapi.controllers;


import com.deliveryapi.models.Address;
import com.deliveryapi.models.Order;
import com.deliveryapi.models.Product;
import com.deliveryapi.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OrderRepository orderRepository;

    private final Order ORDER_1 = new Order(
            1L,
            123L,
            Arrays.asList(new Product(1L, "Smartphone", 599.99)),
            new Address(1L, "Sunset Boulevard", "Los Angeles", "900001"),
            Order.OrderStatus.PROCESSING
    );

    private final Order ORDER_2 = new Order(
            2L,
            456L,
            Arrays.asList(new Product(3L, "Laptop", 999.50), new Product(6L, "Headphones", 149.99)),
            new Address(2L, "Baker Street", "London", "W1U 6TY"),
            Order.OrderStatus.SHIPPED
    );

    private final Order ORDER_3 = new Order(
            3L,
            789L,
            Arrays.asList(new Product(5L, "Tablet", 299.00)),
            new Address(3L, "Champs-Élysées", "Paris", "750008"),
            Order.OrderStatus.PROCESSING
    );

    @Test
    public void testGetAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(ORDER_1);
        orders.add(ORDER_2);
        orders.add(ORDER_3);

        when(orderRepository.findAll()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].status", is("обработка")));
    }

    @Test
    public void testGetOrderById() throws Exception {
        when(orderRepository.findById(ORDER_1.getId())).thenReturn(Optional.of(ORDER_1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.customerId", is(123)));
    }

    @Test
    public void testGetOrderById_NotFound() throws Exception {
        when(orderRepository.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/orders/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetOrdersByStatus() throws Exception {
        List<Order> processingOrders = new ArrayList<>();
        processingOrders.add(ORDER_1);
        processingOrders.add(ORDER_3);

        when(orderRepository.findByStatus(Order.OrderStatus.PROCESSING)).thenReturn(processingOrders);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/orders/findByStatus")
                .param("status", "обработка")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status", is("обработка")));
    }

    @Test
    public void testGetOrdersByStatus_NotFound() throws Exception {
        when(orderRepository.findByStatus(Order.OrderStatus.PROCESSING)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/orders/findByStatus")
                .param("status", "обработка")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order newOrder = new Order(
                4L,
                333L,
                Arrays.asList(new Product(4L, "Test Product", 1234.56)),
                new Address(4L, "Test Street", "Test City", "123456"),
                Order.OrderStatus.DELIVERED
        );
        String requestBody = mapper.writeValueAsString(newOrder);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/delivery/orders")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(orderRepository, times(1)).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        Order updatedOrder = new Order(
                4L,
                222L,
                Arrays.asList(new Product(4L, "Test Product", 6543.21)),
                new Address(4L, "Test Street", "Test City", "654321"),
                Order.OrderStatus.SHIPPED
        );
        String requestBody = mapper.writeValueAsString(updatedOrder);

        when(orderRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/delivery/orders/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void testUpdateOrder_NotFound() throws Exception {
        Order updatedOrder = new Order(
                4L,
                222L,
                Arrays.asList(new Product(4L, "Test Product", 6543.21)),
                new Address(4L, "Test Street", "Test City", "654321"),
                Order.OrderStatus.SHIPPED
        );
        String requestBody = mapper.writeValueAsString(updatedOrder);

        when(orderRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/delivery/orders/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, never()).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        when(orderRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/delivery/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteOrder_NotFound() throws Exception {
        when(orderRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/delivery/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, never()).deleteById(1L);
    }
}
