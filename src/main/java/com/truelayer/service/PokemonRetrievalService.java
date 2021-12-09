package com.truelayer.service;

import com.truelayer.client.pokeapi.PokeAPIClient;
import com.truelayer.client.pokeapi.resource.PokemonResource;
import com.truelayer.configuration.ConfigurationProperties;
import com.truelayer.domain.Pokemon;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PokemonRetrievalService {

    private final PokeAPIClient pokeAPIClient;
    private final ConfigurationProperties configurationProperties;

    @Autowired
    public PokemonRetrievalService(PokeAPIClient pokeAPIClient, ConfigurationProperties configurationProperties) {
        this.pokeAPIClient = pokeAPIClient;
        this.configurationProperties = configurationProperties;
    }

    public Pokemon retrievePokemon(String name) {
        log.info("Retrieving pokemon information, name={}", name);

        PokemonResource pokemon = pokeAPIClient.retrievePokemon(name);

        return Pokemon.builder()
                .name(pokemon.getName())
                .description(
                    StringUtils.normalizeSpace(
                        pokemon.getFlavorTextEntries().stream()
                            .filter(
                                entry -> entry.getLanguage().getName().equals(configurationProperties.getLanguage())
                            )
                            .filter(
                                entry -> entry.getVersion().getName().equals(configurationProperties.getVersion())
                            )
                        .findFirst().get().getFlavorText()
                    )
                )
                .habitat(pokemon.getHabitat().getName())
                .isLegendary(pokemon.isLegendary())
                .build();
    }
}
