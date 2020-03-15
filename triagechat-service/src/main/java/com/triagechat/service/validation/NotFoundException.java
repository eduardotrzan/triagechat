package com.triagechat.service.validation;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String detail) {
        super(String.format("Not found object: %s.", detail));
    }
}
