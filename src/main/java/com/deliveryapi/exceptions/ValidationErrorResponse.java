package com.deliveryapi.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Hidden
public class ValidationErrorResponse {

    private final List<Violation> violations;

}

