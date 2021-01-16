package com.bar.system.error;

public class ClientErrorException extends RuntimeException {

    public ClientErrorException(String message) {
        super(message);
    }
}
