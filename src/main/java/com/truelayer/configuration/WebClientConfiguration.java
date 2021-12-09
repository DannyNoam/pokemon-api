package com.truelayer.configuration;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfiguration {

    @Autowired
    private ConfigurationProperties configurationProperties;

    @Bean
    public WebClient pokeApiWebClient() {
        HttpClient client = HttpClient.create()
                .baseUrl(configurationProperties.getPokeApiUri())
                .responseTimeout(Duration.ofSeconds(configurationProperties.getPokeApiReadTimeoutMs()))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configurationProperties.getPokeApiConnectionTimeoutMs());

        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(client)).build();
    }

    @Bean
    public WebClient funTranslationsWebClient() {
        HttpClient client = HttpClient.create()
                .baseUrl(configurationProperties.getFunTranslationsApiUri())
                .responseTimeout(Duration.ofSeconds(configurationProperties.getFunTranslationsReadTimeoutMs()))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configurationProperties.getFunTranslationsConnectionTimeoutMs());

        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(client)).build();
    }
}
