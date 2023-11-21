package com.global.MedicineNow.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RestNotFoundException extends RuntimeException {

    public RestNotFoundException(String message) {
        super(message);
    }

}