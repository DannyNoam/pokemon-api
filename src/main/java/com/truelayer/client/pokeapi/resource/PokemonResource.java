package com.truelayer.client.pokeapi.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PokemonResource {

    private String name;

    @JsonProperty("is_legendary")
    private boolean legendary;

    @JsonProperty("flavor_text_entries")
    private List<PokemonFlavorText> flavorTextEntries;

    private Habitat habitat;
}
