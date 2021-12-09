package com.truelayer.configuration;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "settings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationProperties {

    private String version;
    private String language;

    @Value("${settings.api.funTranslation.baseUri}")
    private String translationApiUri;

    @Value("${settings.api.pokeApi.baseUri}")
    private String pokeApiUri;

    @Value("${settings.api.pokeApi.connectionTimeoutMs}")
    private int pokeApiConnectionTimeoutMs;

    @Value("${settings.api.pokeApi.connectionTimeoutMs}")
    private int pokeApiReadTimeoutMs;

    @Value("${settings.api.pokeApi.speciesUri}")
    private String pokemonSpeciesUri;

    @Value("${settings.api.funTranslation.baseUri}")
    private String funTranslationsApiUri;

    @Value("${settings.api.funTranslation.connectionTimeoutMs}")
    private int funTranslationsConnectionTimeoutMs;

    @Value("${settings.api.funTranslation.connectionTimeoutMs}")
    private int funTranslationsReadTimeoutMs;

    @Value("${settings.api.funTranslation.shakespeareUri}")
    private String shakespeareTranslationUri;

    @Value("${settings.api.funTranslation.yodaUri}")
    private String yodaTranslationUri;
}
