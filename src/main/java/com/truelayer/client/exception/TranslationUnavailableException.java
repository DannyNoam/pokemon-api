package com.truelayer.client.exception;

import org.springframework.web.reactive.function.client.WebClientRequestException;

public class TranslationUnavailableException extends Exception {

    public TranslationUnavailableException(Exception e) {
        super(e);
    }
}
