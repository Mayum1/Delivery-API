package com.deliveryapi.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Delivery API",
                description = "API для управления заказами доставки", version = "1.0.0"
        )
)

public class SwaggerConfig {
}
