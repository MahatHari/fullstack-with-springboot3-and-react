package com.harimahat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author hari.mahat on 28.7.2023
 * Project learn-spring3
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestValidationException extends RuntimeException {
    public RequestValidationException(String noDataChangesFound) {
        super(noDataChangesFound);
    }
}
