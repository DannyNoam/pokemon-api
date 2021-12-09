package com.truelayer.client.funtranslations;

import com.truelayer.client.exception.TranslationUnavailableException;
import com.truelayer.client.funtranslations.resource.Translation;
import com.truelayer.configuration.ConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class FunTranslationsAPIClient {

    private final ConfigurationProperties configurationProperties;
    private final WebClient client;

    @Autowired
    public FunTranslationsAPIClient(ConfigurationProperties configurationProperties, WebClient funTranslationsWebClient) {
        this.configurationProperties = configurationProperties;
        this.client = funTranslationsWebClient;
    }

    public String translateUsingShakespeare(String description) throws TranslationUnavailableException {
        try {
            return executeRequest(
                    getBaseWebClientWithURI(
                        configurationProperties.getShakespeareTranslationUri()
                    ),
                    description
            );
        } catch(WebClientResponseException e) {
            log.error("API Request to Fun Translations Shakespeare API failed", e);
            throw new TranslationUnavailableException(e);
        }
    }

    public String translateUsingYoda(String description) throws TranslationUnavailableException {
        try {
            return executeRequest(
                    getBaseWebClientWithURI(
                        configurationProperties.getYodaTranslationUri()
                    ),
                    description
                );
        } catch(WebClientResponseException e) {
            log.error("API Request to Fun Translations Yoda API failed", e);
            throw new TranslationUnavailableException(e);
        }
    }

    private WebClient.RequestBodySpec getBaseWebClientWithURI(String uri) {
        return client.post()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-url-encoded")
                .accept(MediaType.APPLICATION_JSON);
    }

    private String executeRequest(WebClient.RequestBodySpec requestBodySpec, String payload) {
            return requestBodySpec
                    .body(BodyInserters.fromFormData("text", payload))
                    .retrieve()
                    .bodyToMono(Translation.class)
                    .block()
                    .getContents()
                    .getTranslated();
    }
}
