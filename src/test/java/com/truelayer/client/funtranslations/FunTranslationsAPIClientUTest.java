package com.truelayer.client.funtranslations;

import com.truelayer.client.exception.TranslationUnavailableException;
import com.truelayer.client.funtranslations.resource.Translation;
import com.truelayer.client.funtranslations.resource.TranslationContents;
import com.truelayer.configuration.ConfigurationProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FunTranslationsAPIClientUTest {

    private static final String YODA_TRANSLATION_URI = "/yoda";
    private static final String SHAKESPEARE_TRANSLATION_URI = "/shakespeare";
    private static final String DESCRIPTION = "A fine pokemon";
    private static final String TRANSLATED_DESCRIPTION = "Some translated content";

    private final ConfigurationProperties configurationProperties = ConfigurationProperties.builder()
            .yodaTranslationUri(YODA_TRANSLATION_URI)
            .shakespeareTranslationUri(SHAKESPEARE_TRANSLATION_URI)
            .build();

    @Mock
    private WebClient client;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Mono<Translation> mono;

    @Captor
    private ArgumentCaptor<BodyInserters.FormInserter> formInserterArgumentCaptor;

    private Translation translation;

    private FunTranslationsAPIClient funTranslationsAPIClient;

    @Before
    public void setup() {
        funTranslationsAPIClient = new FunTranslationsAPIClient(configurationProperties, client);

        translation = new Translation();
        TranslationContents translationContents = new TranslationContents(TRANSLATED_DESCRIPTION);
        translation.setContents(translationContents);

        when(client.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(YODA_TRANSLATION_URI)).thenReturn(requestBodySpec);
        when(requestBodyUriSpec.uri(SHAKESPEARE_TRANSLATION_URI)).thenReturn(requestBodySpec);
        when(requestBodySpec.header("Content-Type", "application/x-www-form-url-encoded")).thenReturn(requestBodySpec);
        when(requestBodySpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(formInserterArgumentCaptor.capture())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Translation.class)).thenReturn(mono);
        when(mono.block()).thenReturn(translation);
    }

    @Test
    public void translateUsingShakespeare_apiCallSucceeds_translatedDescriptionReturned() throws TranslationUnavailableException {
        // when
        String actual = funTranslationsAPIClient.translateUsingShakespeare(DESCRIPTION);

        // then
        assertEquals(TRANSLATED_DESCRIPTION, actual);
    }

    @Test
    public void translateUsingShakespeare_apiCallFails_translationUnavailableExceptionThrown() {
        // given
        when(requestHeadersSpec.retrieve()).thenThrow(WebClientResponseException.class);

        // when
        TranslationUnavailableException thrown = Assertions.assertThrows(TranslationUnavailableException.class, () -> {
            funTranslationsAPIClient.translateUsingShakespeare(DESCRIPTION);
        });
    }

    @Test
    public void translateUsingYoda_apiCallSucceeds_translatedDescriptionReturned() throws TranslationUnavailableException {
        // when
        String actual = funTranslationsAPIClient.translateUsingYoda(DESCRIPTION);

        // then
        assertEquals(TRANSLATED_DESCRIPTION, actual);
    }

    @Test
    public void translateUsingYoda_apiCallFails_translationUnavailableExceptionThrown() {
        // given
        when(requestHeadersSpec.retrieve()).thenThrow(WebClientResponseException.class);

        // when
        TranslationUnavailableException thrown = Assertions.assertThrows(TranslationUnavailableException.class, () -> {
            funTranslationsAPIClient.translateUsingYoda(DESCRIPTION);
        });
    }

}