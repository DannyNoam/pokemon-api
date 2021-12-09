package com.truelayer.functional;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.io.Resources;
import com.truelayer.domain.Pokemon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PokemonApiFunctionalTest {

    private static final String REGULAR_POKEMON_NAME = "ditto";
    private static final String LEGENDARY_POKEMON_NAME = "articuno";
    private static final String CAVE_POKEMON_NAME = "zubat";

    private static final String REGULAR_POKEMON_DESCRIPTION = "Capable of copying an enemy's genetic code to instantly" +
            " transform itself into a duplicate of the enemy.";
    private static final String LEGENDARY_POKEMON_DESCRIPTION = "A legendary bird POKéMON. It freezes water that" +
            " is contained in winter air and makes it snow.";
    private static final String CAVE_POKEMON_DESCRIPTION = "Forms colonies in perpetually dark places. Uses" +
            " ultrasonic waves to identify and approach targets.";

    private static final String POKEMON_LEGENDARY_YODA_TRANSLATED_DESCRIPTION = "A legendary bird pokémon. Water" +
            " that is contained in winter air and makes it snow, it freezes.";
    private static final String POKEMON_CAVE_YODA_TRANSLATED_DESCRIPTION = "Forms colonies in perpetually dark places" +
            ".Ultrasonic waves to identify and approach targets, uses.";
    private static final String POKEMON_REGULAR_SHAKESPEARE_TRANSLATED_DESCRIPTION = "Capable of copying an foe's genetic code" +
            " to instantly transform itself into a duplicate of the foe.";

    private static final int FUN_TRANSLATION_API_PORT = 23029;
    private static final int POKE_API_PORT = 30293;

    private WireMockServer funTranslationApiMockServer = new WireMockServer(FUN_TRANSLATION_API_PORT);
    private WireMockServer pokeApiMockServer = new WireMockServer(POKE_API_PORT);

    private WebClient webClient;

    @LocalServerPort
    private int port;

    @Before
    public void setup() throws IOException {
        webClient = createTestWebClient();

        funTranslationApiMockServer.start();
        pokeApiMockServer.start();

        List<String> allPokemon = Arrays.asList(REGULAR_POKEMON_NAME, LEGENDARY_POKEMON_NAME, CAVE_POKEMON_NAME);

        stubPokeApiPokemonSpeciesEndpointForPokemon(allPokemon);
    }

    @Test
    public void getPokemon_successfullyRetrievesPokemon() {
        // given
        Pokemon expectedPokemon = Pokemon.builder()
            .name(REGULAR_POKEMON_NAME)
            .description(REGULAR_POKEMON_DESCRIPTION)
            .habitat("urban")
            .isLegendary(false)
            .build();

        // when
        Pokemon actual = makeRequestToGetPokemon(webClient);

        // then
        assertEquals(expectedPokemon, actual);
    }

    @Test
    public void getTranslatedPokemon_pokemonIsLegendary_descriptionIsYodaTranslated() throws IOException {
        // given
        stubFunTranslationsEndpoint(LEGENDARY_POKEMON_NAME, LEGENDARY_POKEMON_DESCRIPTION, "yoda");

        Pokemon expectedPokemon = Pokemon.builder()
                .name(LEGENDARY_POKEMON_NAME)
                .description(POKEMON_LEGENDARY_YODA_TRANSLATED_DESCRIPTION)
                .habitat("rare")
                .isLegendary(true)
                .build();

        // when
        Pokemon actual = makeRequestToGetTranslatedPokemon(webClient, LEGENDARY_POKEMON_NAME);

        // then
        assertEquals(expectedPokemon, actual);
    }

    @Test
    public void getTranslatedPokemon_pokemonIsFromCave_descriptionIsYodaTranslated() throws IOException {
        // given
        stubFunTranslationsEndpoint(CAVE_POKEMON_NAME, CAVE_POKEMON_DESCRIPTION, "yoda");

        Pokemon expectedPokemon = Pokemon.builder()
                .name(CAVE_POKEMON_NAME)
                .description(POKEMON_CAVE_YODA_TRANSLATED_DESCRIPTION)
                .habitat("cave")
                .isLegendary(false)
                .build();

        // when
        Pokemon actual = makeRequestToGetTranslatedPokemon(webClient, CAVE_POKEMON_NAME);

        // then
        assertEquals(expectedPokemon, actual);
    }

    @Test
    public void getTranslatedPokemon_pokemonIsNotLegendaryOrFromCave_descriptionIsShakespeareTranslated() throws IOException {
        // given
        stubFunTranslationsEndpoint(REGULAR_POKEMON_NAME, REGULAR_POKEMON_DESCRIPTION, "shakespeare");

        Pokemon expectedPokemon = Pokemon.builder()
                .name(REGULAR_POKEMON_NAME)
                .description(POKEMON_REGULAR_SHAKESPEARE_TRANSLATED_DESCRIPTION)
                .habitat("urban")
                .isLegendary(false)
                .build();

        // when
        Pokemon actual = makeRequestToGetTranslatedPokemon(webClient, REGULAR_POKEMON_NAME);

        // then
        assertEquals(expectedPokemon, actual);
    }

    @After
    public void teardown() {
        funTranslationApiMockServer.stop();
        pokeApiMockServer.stop();
    }

    private WebClient createTestWebClient() {
        WebClient client = WebClient.builder()
                .baseUrl(String.format("http://localhost:%d", port))
                .build();
        return client;
    }

    private Pokemon makeRequestToGetPokemon(WebClient client) {
        return client
                .get()
                .uri("/pokemon/".concat(REGULAR_POKEMON_NAME))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Pokemon.class)
                .block();
    }

    private Pokemon makeRequestToGetTranslatedPokemon(WebClient client, String pokemonName) {
        return client
                .get()
                .uri("/pokemon/translated/".concat(pokemonName))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Pokemon.class)
                .block();
    }

    private void stubPokeApiPokemonSpeciesEndpointForPokemon(List<String> pokemons) throws IOException {
        for(String pokemon : pokemons) {
            pokeApiMockServer.stubFor(get(urlEqualTo("/pokemon-species/".concat(pokemon)))
                    .withHeader("accept", equalTo("application/json"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(loadFixtureAsString(String.format("fixtures/pokeapi/pokemon-species-%s.json", pokemon)))));
        }
    }

    private void stubFunTranslationsEndpoint(String pokemonName, String pokemonDescription, String translation) throws IOException {
         funTranslationApiMockServer.stubFor(post(urlEqualTo("/" + translation.concat(".json")))
                    .withHeader("accept", equalTo("application/json"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(loadFixtureAsString(String.format("fixtures/funtranslations/%s-%s.json", translation, pokemonName)))));
    }

    private String loadFixtureAsString(String filePath) throws IOException {
        return Resources.toString(Resources.getResource(filePath), StandardCharsets.UTF_8);
    }
}
