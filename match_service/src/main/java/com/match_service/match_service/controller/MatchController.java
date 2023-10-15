package com.match_service.match_service.controller;

import java.time.Duration;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.match_service.match_service.model.Match;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@RestController
@RequestMapping("/matchs")
public class MatchController {

    private static final CircuitBreaker circuitBreaker;

    @Autowired
	RestTemplate restTemplate;


    public MatchController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    static {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowSize(2)
                .build();

        circuitBreaker = CircuitBreaker.of("match-service", circuitBreakerConfig);
    }
    static ArrayList<Match> listeMatchs = new ArrayList<Match>() {
        {
            add(new Match(1, "SCB", "OM"));
            add(new Match(2, "PSG", "ACA"));
            add(new Match(3, "OM", "SCB"));
            add(new Match(4, "ACA", "SCB"));
        }
    };

    @GetMapping("/{id}")
    public ResponseEntity<String> getMatch(@PathVariable int id) {

        try {
            return circuitBreaker.executeSupplier(() -> getMatchById(id));
        } catch (Exception e) {
            return fallback("Équipe non trouvée");
        }
    }

    private ResponseEntity<String> getMatchById(int id) {
        for (Match match : listeMatchs) {
            if (match.getId() == id) {
                return new ResponseEntity<>(match.getName(), HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }
    
    @PostMapping
    public ResponseEntity<String> createMatch(@RequestBody Match match) {
        try {
            return circuitBreaker.executeSupplier(() -> createAndAddMatch(match));
        } catch (Exception e) {
            return fallback("Cette action n'a pas été faite");
        }
    }

    private ResponseEntity<String> createAndAddMatch(Match match) {
        listeMatchs.add(match);
        return new ResponseEntity<>("Équipe créée : " + match.getName(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMatch(@PathVariable int id, @RequestBody Match match) {
        try {
            return circuitBreaker.executeSupplier(() -> updateExistingMatch(id, match));
        } catch (Exception e) {
            return fallback("Pas pu mettre à jour");
        }
    }

    private ResponseEntity<String> updateExistingMatch(int id, Match match) {
        for (Match currentMatch : listeMatchs) {
            if (currentMatch.getId() == id) {
                currentMatch.setNameEquipe1(match.getNameEquipe1());
                currentMatch.setNameEquipe2(match.getNameEquipe2());
                return new ResponseEntity<String>(currentMatch.getName(), HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable int id) {
        try {
            return circuitBreaker.executeSupplier(() -> deleteExistingMatch(id));
        } catch (Exception e) {
            return fallback("Pas pu supprimé");
        }
    }

    private ResponseEntity<String> deleteExistingMatch(int id) {
        for (Match match : listeMatchs) {
            if (match.getId() == id) {
                listeMatchs.remove(match);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }


    private ResponseEntity<String> fallback(String mess) {
        // Vous pouvez personnaliser cette réponse en fonction de vos besoins
        return new ResponseEntity<>(mess, HttpStatus.NOT_FOUND);
    }

}
