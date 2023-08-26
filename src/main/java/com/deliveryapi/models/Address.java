package com.deliveryapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotEmpty(message = "Укажите улицу")
    private String street;

    @Schema(description = "Город", example = "г. Нижний Новгород")
    @NotEmpty(message = "Укажите город")
    private String city;

    @Schema(description = "Индекс", example = "603022")
    @NotEmpty(message = "Укажите индекс")
    @Pattern(regexp = "\\d{6}", message = "Индекс должен состоять из 6 цифр")
    private String zipCode;

}
