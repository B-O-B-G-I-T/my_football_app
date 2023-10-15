package com.stats_service.stats_service.controller;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@RestController
public class StatistiqueController {

    @Autowired
    RestTemplate restTemplate;

    public StatistiqueController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // Déclaration du circuit breaker
    private static final CircuitBreaker circuitBreaker;

    // Configuration du circuit breaker
    static {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowSize(2)
                .build();

        // Initialisation du circuit breaker avec la configuration définie
        circuitBreaker = CircuitBreaker.of("stats-service", circuitBreakerConfig);
    }

    // Méthode pour obtenir les stats du joueur par son ID
    @GetMapping("/player-stats/{id}")
    public ResponseEntity<String> getPlayer(@PathVariable int id) {

        try {
            // Utilisation du circuit breaker pour exécuter la méthode getPlayerById
            return circuitBreaker.executeSupplier(() -> getStatPlayerById(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Joueur non trouvé");
        }
    }

    // Méthode pour obtenir un joueur par son ID
    private ResponseEntity<String> getStatPlayerById(int id) {
        try {
            String playersServiceUrl = "http://microservice-player/players/Stats/" + id;
            String player = this.restTemplate.getForObject(playersServiceUrl, String.class);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
                throw new RuntimeException("Joueur non trouvé");
            }
            throw new RuntimeException("Une erreur inattendue s'est produite");
        }
    }

    @GetMapping("/team-stats/{id}")
    public ResponseEntity<String> getTeam(@PathVariable int id) {

        try {
            // Utilisation du circuit breaker pour exécuter la méthode getPlayerById
            return circuitBreaker.executeSupplier(() -> getStatTeamById(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Équipe non trouvé");
        }
    }

    // Méthode pour obtenir un joueur par son ID
    private ResponseEntity<String> getStatTeamById(int id) {
        try {
            String playersServiceUrl = "http://microservice-team/teams/Stats/" + id;
            String player = this.restTemplate.getForObject(playersServiceUrl, String.class);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
                throw new RuntimeException("Équipe non trouvé");
            }
            throw new RuntimeException("Une erreur inattendue s'est produite");
        }
    }


    // Méthode de repli en cas d'erreur
    private ResponseEntity<String> fallback(String mess) {
        // Vous pouvez personnaliser cette réponse en fonction de vos besoins
        return new ResponseEntity<>(mess, HttpStatus.NOT_FOUND);
    }

    // @GetMapping("player-stats/{playerId}")

}
