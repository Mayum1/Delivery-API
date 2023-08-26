package com.deliveryapi.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Hidden
public class Violation {

    private final String fieldName;
    private final String message;

}
