package com.team_service.team_service.controller;

// Importation des packages nécessaires

import java.time.Duration;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.team_service.team_service.model.Team;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

// Annotation pour indiquer que cette classe est un contrôleur REST
@RestController
// Chemin de base pour toutes les méthodes de ce contrôleur
@RequestMapping("/teams")
public class TeamController {

    // Déclaration du circuit breaker
    private static final CircuitBreaker circuitBreaker;

    @Autowired
    RestTemplate restTemplate;

    public TeamController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // Configuration du circuit breaker
    static {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowSize(2)
                .build();

        // Initialisation du circuit breaker avec la configuration définie
        circuitBreaker = CircuitBreaker.of("team-service", circuitBreakerConfig);
    }

    // Liste des équipes
    static ArrayList<Team> listeTeams = new ArrayList<Team>() {
        {
            add(new Team(1, "SCB"));
            add(new Team(2, "PSG"));
            add(new Team(3, "OM"));
            add(new Team(4, "ACA"));
        }
    };

    // Méthode pour obtenir une équipe par son ID
    @GetMapping("/{id}")
    public ResponseEntity<String> getTeam(@PathVariable int id) {

        try {
            // Utilisation du circuit breaker pour exécuter la méthode getTeamById
            return circuitBreaker.executeSupplier(() -> getTeamById(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Équipe non trouvée");
        }
    }

    // Méthode pour obtenir une équipe par son ID
    private ResponseEntity<String> getTeamById(int id) {
        for (Team team : listeTeams) {
            if (team.getId() == id) {
                return new ResponseEntity<>(team.teamName, HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

    // Méthode pour obtenir un joueur par son ID
    @GetMapping("/player/{id}")
    public ResponseEntity<String> getPlayer(@PathVariable int id) {

        try {
            // Utilisation du circuit breaker pour exécuter la méthode getPlayerById
            return circuitBreaker.executeSupplier(() -> getPlayerById(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Joueur non trouvé");
        }
    }

    // Méthode pour obtenir un joueur par son ID
    private ResponseEntity<String> getPlayerById(int id) {
        try {
            String playersServiceUrl = "http://microservice-player/players/" + id;
            String player = this.restTemplate.getForObject(playersServiceUrl, String.class);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
                throw new RuntimeException("Joueur non trouvé");
            }
            throw new RuntimeException("Une erreur inattendue s'est produite");
        }
    }

    // Méthode pour créer une équipe
    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody Team team) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode createAndAddTeam
            return circuitBreaker.executeSupplier(() -> createAndAddTeam(team));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Cette action n'a pas été faite");
        }
    }

    // Méthode pour créer une équipe et l'ajouter à la liste des équipes
    private ResponseEntity<String> createAndAddTeam(Team team) {
        listeTeams.add(team);
        return new ResponseEntity<>("Équipe créée : " + team.getName(), HttpStatus.CREATED);
    }

    // Méthode pour mettre à jour une équipe existante
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable int id, @RequestBody Team team) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode updateExistingTeam
            return circuitBreaker.executeSupplier(() -> updateExistingTeam(id, team));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Pas pu mettre à jour");
        }
    }

    // Méthode pour mettre à jour une équipe existante
    private ResponseEntity<String> updateExistingTeam(int id, Team team) {
        for (Team currentTeam : listeTeams) {
            if (currentTeam.getId() == id) {
                currentTeam.setName(team.getName());
                return new ResponseEntity<String>(currentTeam.teamName, HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

    // Méthode pour supprimer une équipe existante
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable int id) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode deleteExistingTeam
            return circuitBreaker.executeSupplier(() -> deleteExistingTeam(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Pas pu supprimé");
        }
    }

    // Méthode pour supprimer une équipe existante de la liste des équipes
    private ResponseEntity<String> deleteExistingTeam(int id) {
        for (Team team : listeTeams) {
            if (team.getId() == id) {
                listeTeams.remove(team);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

    // Méthode de repli en cas d'erreur
    private ResponseEntity<String> fallback(String mess) {
        // Vous pouvez personnaliser cette réponse en fonction de vos besoins
        return new ResponseEntity<>(mess, HttpStatus.NOT_FOUND);
    }

}
