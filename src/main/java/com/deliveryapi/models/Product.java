package com.deliveryapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Schema(description = "Сущность товара",
        example = "{\"name\": \"Подушка\"," +
                "\"price\": 699.99}")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор", example = "13", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Название товара", example = "Подушка")
    @NotEmpty(message = "Укажите название товара")
    private String name;

    @Schema(description = "Цена товара", example = "699.99")
    @NotNull
    @Min(value = 0, message = "Цена не должна быть меньше 0")
    private double price;

}
