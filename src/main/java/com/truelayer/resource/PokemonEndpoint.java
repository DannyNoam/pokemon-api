package com.truelayer.resource;

import com.truelayer.domain.Pokemon;
import com.truelayer.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PokemonEndpoint {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonEndpoint(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping(
            value="/pokemon/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Pokemon getPokemon(@PathVariable String name) {
        return pokemonService.getPokemonByName(name);
    }

    @GetMapping(
            value="/pokemon/translated/{name}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Pokemon getPokemonWithTranslatedDescription(@PathVariable String name) {
        return pokemonService.getPokemonWithTranslatedDescription(name);
    }
}
