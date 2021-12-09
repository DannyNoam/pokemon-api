package com.truelayer.service;

import com.truelayer.client.exception.TranslationUnavailableException;
import com.truelayer.client.funtranslations.FunTranslationsAPIClient;
import com.truelayer.domain.Pokemon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PokemonDescriptionTranslationServiceUTest {

    private static final String ORIGINAL_DESCRIPTION = "He has a lot to learn.";
    private static final String YODA_TRANSLATED_DESCRIPTION = "A lot to learn, he has.";
    private static final String SHAKESPEARE_TRANSLATED_DESCRIPTION = "Thou has a lot to learn.";

    private Pokemon pokemon = Pokemon.builder()
            .description(ORIGINAL_DESCRIPTION)
            .build();

    @Mock
    private FunTranslationsAPIClient funTranslationsAPIClient;

    @InjectMocks
    private PokemonDescriptionTranslationService pokemonDescriptionTranslationService;

    @Test
    public void translateDescriptionUsingShakespeare_apiCallSucceeds_returnsPokemonWithShakespeareTranslatedDescription() throws TranslationUnavailableException {
        // given
        when(funTranslationsAPIClient.translateUsingShakespeare(ORIGINAL_DESCRIPTION)).thenReturn(SHAKESPEARE_TRANSLATED_DESCRIPTION);
        Pokemon expectedPokemon = Pokemon.builder()
                .description(SHAKESPEARE_TRANSLATED_DESCRIPTION)
                .build();

        // when
        Pokemon actual = pokemonDescriptionTranslationService.translateDescriptionUsingShakespeare(pokemon);

        // then
        assertEquals(expectedPokemon, actual);
    }

    @Test
    public void translateDescriptionUsingShakespeare_translationUnavailableExceptionThrown_returnsPokemonWithOriginalDescription() throws TranslationUnavailableException {
        // given
        when(funTranslationsAPIClient.translateUsingShakespeare(ORIGINAL_DESCRIPTION)).thenThrow(TranslationUnavailableException.class);

        // when
        Pokemon actual = pokemonDescriptionTranslationService.translateDescriptionUsingShakespeare(pokemon);

        // then
        assertEquals(pokemon, actual);
    }

    @Test
    public void translateDescriptionUsingYoda_apiCallSucceeds_returnsPokemonWithYodaTranslatedDescription() throws TranslationUnavailableException {
        // given
        when(funTranslationsAPIClient.translateUsingYoda(ORIGINAL_DESCRIPTION)).thenReturn(YODA_TRANSLATED_DESCRIPTION);
        Pokemon expectedPokemon = Pokemon.builder()
                .description(YODA_TRANSLATED_DESCRIPTION)
                .build();

        // when
        Pokemon actual = pokemonDescriptionTranslationService.translateDescriptionUsingYoda(pokemon);

        // then
        assertEquals(expectedPokemon, actual);
    }

    @Test
    public void translateDescriptionUsingYoda_translationUnavailableExceptionThrown_returnsPokemonWithOriginalDescription() throws TranslationUnavailableException {
        // given
        when(funTranslationsAPIClient.translateUsingYoda(ORIGINAL_DESCRIPTION)).thenThrow(TranslationUnavailableException.class);

        // when
        Pokemon actual = pokemonDescriptionTranslationService.translateDescriptionUsingYoda(pokemon);

        // then
        assertEquals(pokemon, actual);
    }
}