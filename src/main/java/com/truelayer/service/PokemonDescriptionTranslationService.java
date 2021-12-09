package com.truelayer.service;

import com.truelayer.client.exception.TranslationUnavailableException;
import com.truelayer.client.funtranslations.FunTranslationsAPIClient;
import com.truelayer.domain.Pokemon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PokemonDescriptionTranslationService {

    private static final String TRANSLATION_ERROR_LOG = "Couldn't translate description for pokemon={}, description will remain unchanged";

    private final FunTranslationsAPIClient funTranslationsAPIClient;

    @Autowired
    public PokemonDescriptionTranslationService(FunTranslationsAPIClient funTranslationsAPIClient) {
        this.funTranslationsAPIClient = funTranslationsAPIClient;
    }

    public Pokemon translateDescriptionUsingShakespeare(Pokemon pokemon) {
        try {
            String translatedDescription = funTranslationsAPIClient
                    .translateUsingShakespeare(pokemon.getDescription());
            pokemon.setDescription(translatedDescription);
            log.info("Applied Shakespeare translation={} for pokemon={}", translatedDescription, pokemon.getName());
        } catch (TranslationUnavailableException e) {
            log.error(TRANSLATION_ERROR_LOG, pokemon.getName());
        }

        return pokemon;
    }

    public Pokemon translateDescriptionUsingYoda(Pokemon pokemon) {
        try {
            String translatedDescription = funTranslationsAPIClient
                    .translateUsingYoda(pokemon.getDescription());
            pokemon.setDescription(translatedDescription);
            log.info("Applied Yoda translation={} for pokemon={}", translatedDescription, pokemon.getName());
        } catch (TranslationUnavailableException e) {
            log.error(TRANSLATION_ERROR_LOG, pokemon.getName());
        }

        return pokemon;
    }
}
