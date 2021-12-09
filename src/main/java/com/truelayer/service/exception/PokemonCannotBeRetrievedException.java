package com.truelayer.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class PokemonCannotBeRetrievedException extends RuntimeException {
    public PokemonCannotBeRetrievedException(String name, Exception e) {
        super(String.format("The Pokemon %s couldn't be retrieved!", name), e);
    }
}
