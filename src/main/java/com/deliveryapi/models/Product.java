package com.deliveryapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.persistence.*;
import lombok.*;

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
    private String name;

    @Min(0) @Schema(description = "Цена товара", example = "699.99")
    private double price;

}
