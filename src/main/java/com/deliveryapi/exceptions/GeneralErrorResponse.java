package com.deliveryapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralErrorResponse {
    private final String error;
}
