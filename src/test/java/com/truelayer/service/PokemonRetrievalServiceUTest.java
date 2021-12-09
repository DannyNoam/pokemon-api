package com.truelayer.service;

import com.truelayer.client.pokeapi.PokeAPIClient;
import com.truelayer.client.pokeapi.resource.*;
import com.truelayer.configuration.ConfigurationProperties;
import com.truelayer.domain.Pokemon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PokemonRetrievalServiceUTest {

    private static final String LANGUAGE = "en";
    private static final String VERSION = "red";
    private static final String POKEMON_NAME = "charmander";
    private static final String HABITAT = "grassland";
    private static final String DENORMALIZED_DESCRIPTION = "What \na \nfine   \nspecimen!";
    private static final String NORMALIZED_DESCRIPTION = "What a fine specimen!";
    private static final boolean IS_LEGENDARY = true;

    private ConfigurationProperties configurationProperties = ConfigurationProperties.builder()
            .language(LANGUAGE)
            .version(VERSION)
            .build();

    @Mock
    private PokeAPIClient pokeAPIClient;

    private PokemonRetrievalService pokemonRetrievalService;

    @Before
    public void setup() {
        pokemonRetrievalService = new PokemonRetrievalService(pokeAPIClient, configurationProperties);
    }

    @Test
    public void retrievePokemon_returnsPokemonDomainObject() {
        // given
        PokemonResource pokemonResource = PokemonResource.builder()
            .name(POKEMON_NAME)
            .habitat(new Habitat(HABITAT))
            .legendary(IS_LEGENDARY)
            .flavorTextEntries(pokemonFlavorTextList())
            .build();

        Pokemon expected = Pokemon.builder()
                .description(NORMALIZED_DESCRIPTION)
                .isLegendary(IS_LEGENDARY)
                .habitat(HABITAT)
                .name(POKEMON_NAME)
                .build();

        when(pokeAPIClient.retrievePokemon(POKEMON_NAME)).thenReturn(pokemonResource);

        // when
        Pokemon actual = pokemonRetrievalService.retrievePokemon(POKEMON_NAME);

        // then
        assertEquals(expected, actual);
    }

    private List<PokemonFlavorText> pokemonFlavorTextList() {
        PokemonFlavorText incorrectLanguagePokemonFlavorText = PokemonFlavorText.builder()
                .language(new Language("de"))
                .version(new Version(VERSION))
                .flavorText("A terrible pokemon!")
                .build();

        PokemonFlavorText incorrectVersionPokemonFlavorText = PokemonFlavorText.builder()
                .language(new Language(LANGUAGE))
                .version(new Version("blue"))
                .flavorText("An awful pokemon!")
                .build();

        PokemonFlavorText correctPokemonFlavorText = PokemonFlavorText.builder()
                .language(new Language(LANGUAGE))
                .version(new Version(VERSION))
                .flavorText(DENORMALIZED_DESCRIPTION)
                .build();

        return Arrays.asList(
                incorrectLanguagePokemonFlavorText,
                incorrectVersionPokemonFlavorText,
                correctPokemonFlavorText
        );
    }
}