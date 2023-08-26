package com.deliveryapi.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Schema(description = "Сущность заказа",
        example = "{\"customerId\": 3," +
                "\"products\": [{\"id\": 4}]," +
                "\"shippingAddress\": {\"id\": 1}," +
                "\"status\": \"обработка\"}")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Идентификатор пользователя-заказчика")
    @NotNull(message = "Укажите идентификатор пользователя-заказчика")
    private Long customerId;

    @ManyToMany
    @JoinTable(name = "order_products")
    @Schema(description = "Заказанные товары", example = "[{\"id\": 4}]")
    @NotEmpty(message = "Корзина не может быть пуста")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @Schema(description = "Адрес доставки", example = "{\"id\": 1}")
    @NotNull(message = "Укажите id адреса")
    private Address shippingAddress;

    @Schema(description = "Статус доставки", example = "обработка")
    @NotNull(message = "Укажите статус заказа")
    private OrderStatus status;

    public enum OrderStatus {
        PROCESSING("обработка"),
        SHIPPED("отгружено"),
        DELIVERED("доставлено"),
        CANCELLED("отменено");

        private String value;

        OrderStatus(String value) {
            this.value = value;
        }

        @JsonCreator
        public static OrderStatus fromValue(String value) {
            for (OrderStatus b : OrderStatus.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }

}
