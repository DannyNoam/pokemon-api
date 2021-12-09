package com.truelayer.resource;

import com.truelayer.domain.Pokemon;
import com.truelayer.service.PokemonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PokemonEndpointUTest {

    private static final String POKEMON_NAME = "charmander";

    @Mock
    private PokemonService pokemonService;

    @Mock
    private Pokemon pokemon;

    @InjectMocks
    private PokemonEndpoint pokemonEndpoint;

    @Test
    public void getPokemon_callsPokemonServiceAndReturnsPokemon() {
        // given
        when(pokemonService.getPokemonByName(POKEMON_NAME)).thenReturn(pokemon);

        // when
        Pokemon actual = pokemonEndpoint.getPokemon(POKEMON_NAME);

        // then
        verify(pokemonService).getPokemonByName(POKEMON_NAME);
        assertEquals(pokemon, actual);
    }

    @Test
    public void getPokemonWithTranslatedDescription_callsPokemonServiceAndReturnsPokemon() {
        // given
        when(pokemonService.getPokemonWithTranslatedDescription(POKEMON_NAME)).thenReturn(pokemon);

        // when
        Pokemon actual = pokemonEndpoint.getPokemonWithTranslatedDescription(POKEMON_NAME);

        // then
        verify(pokemonService).getPokemonWithTranslatedDescription(POKEMON_NAME);
        assertEquals(pokemon, actual);
    }
}