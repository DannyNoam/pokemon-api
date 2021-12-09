package com.truelayer.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class PokemonNotFoundException extends RuntimeException{
    public PokemonNotFoundException(String name, Exception e) {
        super(String.format("The Pokemon %s wasn't found!", name), e);
    }
}
