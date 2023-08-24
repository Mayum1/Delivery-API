package com.deliveryapi.controllers;

import com.deliveryapi.models.Order;
import com.deliveryapi.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/delivery/orders")
@Tag(name="Контроллер доставок", description = "Контроллер управления заказами доставки")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Получить все доставки",
            description = "Позволяет получить информацию о всех созданных доставках"
    )
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        return orders != null && !orders.isEmpty()
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Получить заказ доставки",
            description = "Позволяет получить информацию о доставке по её id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable @Parameter(description = "Идентификатор доставки") Long id
    ) {
        Order order = orderService.getOrderById(id);

        return order != null
                ? new ResponseEntity<>(order, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Получить все доставки с заданным статусом",
            description = "Позволяет получить информацию о всех доставках с заданным статусом"
    )
    @GetMapping("/findByStatus")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @RequestParam @Parameter(description = "Статус доставки", style = ParameterStyle.FORM,
                    schema = @Schema(type = "string", defaultValue = "обработка",
                            allowableValues = {"обработка", "отгружено", "доставлено", "отменено"})) String status
    ) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return orders != null && !orders.isEmpty()
                ? new ResponseEntity<>(orders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Создать заказ доставки",
            description = "Создает новую доставку"
    )
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody Order order
    ) {
        orderService.createOrder(order);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить заказ доставки",
            description = "Изменяет информацию созданной доставки"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable @Parameter(description = "Идентификатор доставки") Long id,
            @RequestBody Order order
    ) {
        final boolean updated = orderService.updateOrder(id, order);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
            summary = "Удалить заказ доставки",
            description = "Удаляет доставку по её id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable @Parameter(description = "Идентификатор доставки") Long id
    ) {
        final boolean deleted = orderService.deleteOrder(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
