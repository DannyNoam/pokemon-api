package com.truelayer.service;

import com.truelayer.domain.Pokemon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PokemonService {

    private final PokemonRetrievalService pokemonRetrievalService;
    private final PokemonDescriptionTranslationService pokemonDescriptionTranslationService;

    public PokemonService(PokemonRetrievalService pokemonRetrievalService,
                          PokemonDescriptionTranslationService pokemonDescriptionTranslationService) {
        this.pokemonRetrievalService = pokemonRetrievalService;
        this.pokemonDescriptionTranslationService = pokemonDescriptionTranslationService;
    }

    @Cacheable(value = "pokemon")
    public Pokemon getPokemonByName(String name) {
        return pokemonRetrievalService.retrievePokemon(name);
    }

    @Cacheable(value = "translatedPokemon")
    public Pokemon getPokemonWithTranslatedDescription(String name) {
        Pokemon pokemon = pokemonRetrievalService.retrievePokemon(name);

        return applyTranslatedDescription(pokemon);
    }

    private Pokemon applyTranslatedDescription(Pokemon pokemon) {
        if(pokemon.isLegendary() || (pokemon.getHabitat() != null && pokemon.getHabitat().equals("cave"))) {
            log.info("Applying Yoda translation for pokemon={}", pokemon.getName());
            return pokemonDescriptionTranslationService.translateDescriptionUsingYoda(pokemon);
        } else {
            log.info("Applying Shakespeare translation for pokemon={}", pokemon.getName());
            return pokemonDescriptionTranslationService.translateDescriptionUsingShakespeare(pokemon);
        }
    }
}
