package com.pequla.data.ex;

import lombok.Getter;

@Getter
public class BackendException extends RuntimeException {
    private final int status;

    public BackendException(String message, int status) {
        super(message);
        this.status = status;
    }
}
