package com.truelayer.client.pokeapi.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonRootName("flavor_text_entries")
public class PokemonFlavorText {

    @JsonProperty("flavor_text")
    private String flavorText;

    private Language language;

    private Version version;
}
