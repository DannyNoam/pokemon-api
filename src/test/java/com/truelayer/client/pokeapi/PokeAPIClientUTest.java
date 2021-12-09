package com.truelayer.client.pokeapi;

import com.truelayer.client.pokeapi.resource.PokemonResource;
import com.truelayer.configuration.ConfigurationProperties;
import com.truelayer.service.exception.PokemonCannotBeRetrievedException;
import com.truelayer.service.exception.PokemonNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PokeAPIClientUTest {

    private static final String POKEMON_SPECIES_URL = "/pokemon-species";
    private static final String POKEMON_NAME = "onix";

    private final ConfigurationProperties configurationProperties = ConfigurationProperties.builder()
            .pokemonSpeciesUri(POKEMON_SPECIES_URL)
            .build();

    @Mock
    private WebClient client;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.UriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Mono<PokemonResource> mono;

    @Mock
    private PokemonResource pokemonResource;

    private PokeAPIClient pokeAPIClient;

    @Before
    public void setup() {
        pokeAPIClient = new PokeAPIClient(configurationProperties, client);

        when(client.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(POKEMON_SPECIES_URL.concat("/{name}"), POKEMON_NAME)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
    }

    @Test
    public void retrievePokemon_apiCallSucceeds_pokemonResourceReturned() {
        // given
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PokemonResource.class)).thenReturn(mono);
        when(mono.block()).thenReturn(pokemonResource);

        // when
        PokemonResource actual = pokeAPIClient.retrievePokemon(POKEMON_NAME);

        // then
        assertEquals(pokemonResource, actual);
    }

    @Test
    public void retrievePokemon_apiCallFailsWith404_pokemonNotFoundExceptionThrown() {
        // given
        when(requestHeadersSpec.retrieve()).thenThrow(new WebClientResponseException(404, null, null, null, null));

        // when
        PokemonNotFoundException thrown = Assertions.assertThrows(PokemonNotFoundException.class, () -> {
            pokeAPIClient.retrievePokemon(POKEMON_NAME);
        });

        // then
        assertEquals(String.format("The Pokemon %s wasn't found!", POKEMON_NAME), thrown.getMessage());
    }

    @Test
    public void retrievePokemon_apiCallFailsWithAnyNon404StatusCode_pokemonCannotBeRetrievedExceptionThrown() {
        // given
        when(requestHeadersSpec.retrieve()).thenThrow(new WebClientResponseException(500, null, null, null, null));

        // when
        PokemonCannotBeRetrievedException thrown = Assertions.assertThrows(PokemonCannotBeRetrievedException.class, () -> {
            pokeAPIClient.retrievePokemon(POKEMON_NAME);
        });

        // then
        assertEquals(String.format("The Pokemon %s couldn't be retrieved!", POKEMON_NAME), thrown.getMessage());
    }
}