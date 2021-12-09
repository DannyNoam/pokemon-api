package com.truelayer.service;

import com.truelayer.domain.Pokemon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PokemonServiceUTest {

    private static final String POKEMON_NAME = "charmander";

    private final Pokemon vanillaPokemon = Pokemon.builder().build();
    private final Pokemon legendaryPokemon = Pokemon.builder().isLegendary(true).build();
    private final Pokemon cavePokemon = Pokemon.builder().habitat("cave").build();
    private final Pokemon yodaTranslatedPokemon = Pokemon.builder().description("Much to learn, you still have.").build();
    private final Pokemon shakespeareTranslatedPokemon = Pokemon.builder().description("Tis art, thou?").build();

    @Mock
    private PokemonRetrievalService pokemonRetrievalService;

    @Mock
    private PokemonDescriptionTranslationService pokemonDescriptionTranslationService;

    @InjectMocks
    public PokemonService pokemonService;

    @Test
    public void getPokemonByName_callsUnderlyingServiceAndReturnsPokemon() {
        // given
        when(pokemonRetrievalService.retrievePokemon(POKEMON_NAME)).thenReturn(vanillaPokemon);

        // when
        Pokemon actual = pokemonService.getPokemonByName(POKEMON_NAME);

        // then
        verify(pokemonRetrievalService).retrievePokemon(POKEMON_NAME);
        assertEquals(vanillaPokemon, actual);
    }

    @Test
    public void getPokemonWithTranslatedDescription_pokemonIsLegendary_pokemonHasYodaTranslatedDescription() {
        // given
        when(pokemonRetrievalService.retrievePokemon(POKEMON_NAME)).thenReturn(legendaryPokemon);
        when(pokemonDescriptionTranslationService.translateDescriptionUsingYoda(legendaryPokemon)).thenReturn(yodaTranslatedPokemon);

        // when
        Pokemon actual = pokemonService.getPokemonWithTranslatedDescription(POKEMON_NAME);

        // then
        verify(pokemonRetrievalService).retrievePokemon(POKEMON_NAME);
        verify(pokemonDescriptionTranslationService).translateDescriptionUsingYoda(legendaryPokemon);
        assertEquals(yodaTranslatedPokemon, actual);
    }

    @Test
    public void getPokemonWithTranslatedDescription_pokemonHasCaveHabitat_pokemonHasYodaTranslatedDescription() {
        // given
        when(pokemonRetrievalService.retrievePokemon(POKEMON_NAME)).thenReturn(cavePokemon);
        when(pokemonDescriptionTranslationService.translateDescriptionUsingYoda(cavePokemon)).thenReturn(yodaTranslatedPokemon);

        // when
        Pokemon actual = pokemonService.getPokemonWithTranslatedDescription(POKEMON_NAME);

        // then
        verify(pokemonRetrievalService).retrievePokemon(POKEMON_NAME);
        verify(pokemonDescriptionTranslationService).translateDescriptionUsingYoda(cavePokemon);
        assertEquals(yodaTranslatedPokemon, actual);
    }

    @Test
    public void getPokemonWithTranslatedDescription_pokemonNotLegendaryOrFromCave_pokemonHasShakespeareTranslatedDescription() {
        // given
        when(pokemonRetrievalService.retrievePokemon(POKEMON_NAME)).thenReturn(vanillaPokemon);
        when(pokemonDescriptionTranslationService.translateDescriptionUsingShakespeare(vanillaPokemon)).thenReturn(shakespeareTranslatedPokemon);

        // when
        Pokemon actual = pokemonService.getPokemonWithTranslatedDescription(POKEMON_NAME);

        // then
        verify(pokemonRetrievalService).retrievePokemon(POKEMON_NAME);
        verify(pokemonDescriptionTranslationService).translateDescriptionUsingShakespeare(vanillaPokemon);
        assertEquals(shakespeareTranslatedPokemon, actual);
    }
}