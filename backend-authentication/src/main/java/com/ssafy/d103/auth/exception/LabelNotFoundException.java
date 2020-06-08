package com.ssafy.d103.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LabelNotFoundException extends RuntimeException {
    private Long id;

    public LabelNotFoundException(Long id){
        super(String.format("Label is not found with id = %s", id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
