package com.deliveryapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
@Schema(description = "Сущность адреса",
        example = "{\"street\": \"ул. Пушкина\"," +
                "\"city\": \"г. Нижний Новгород\"," +
                "\"zipCode\": \"603022\"}")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор", example = "75", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Улица", example = "ул. Пушкина")
    private String street;

    @Schema(description = "Город", example = "г. Нижний Новгород")
    private String city;

    @Schema(description = "Индекс", example = "603022")
    private String zipCode;

}
