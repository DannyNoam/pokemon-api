package com.truelayer.client.pokeapi;

import com.truelayer.client.pokeapi.resource.PokemonResource;
import com.truelayer.configuration.ConfigurationProperties;
import com.truelayer.service.exception.PokemonCannotBeRetrievedException;
import com.truelayer.service.exception.PokemonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class PokeAPIClient {

    private final ConfigurationProperties configurationProperties;
    private final WebClient client;

    @Autowired
    public PokeAPIClient(ConfigurationProperties configurationProperties, WebClient pokeApiWebClient) {
        this.configurationProperties = configurationProperties;
        this.client = pokeApiWebClient;
    }

    public PokemonResource retrievePokemon(String name) {
        try {
            return client.get()
                    .uri(configurationProperties.getPokemonSpeciesUri().concat("/{name}"), name)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(PokemonResource.class)
                    .block();
        } catch(WebClientResponseException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.error("No pokemon found for name={}", name, e);
                throw new PokemonNotFoundException(name, e);
            }

            log.error("Unable to retrieve pokemon with name={}", name, e);
            throw new PokemonCannotBeRetrievedException(name, e);
        }
    }
}
