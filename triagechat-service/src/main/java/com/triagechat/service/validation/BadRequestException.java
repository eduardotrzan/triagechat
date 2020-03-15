package com.triagechat.service.validation;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String detail) {
        super(String.format("Bad request object: %s.", detail));
    }
}
