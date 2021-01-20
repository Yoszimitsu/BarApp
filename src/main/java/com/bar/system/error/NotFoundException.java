package com.bar.system.error;

import lombok.Getter;

@Getter
public class NotFoundException extends ClientErrorException {

    private final String id;

    public NotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }
}
