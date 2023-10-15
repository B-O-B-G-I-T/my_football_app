package com.player_service.player_service.controller;

// Importation des packages nécessaires

import java.time.Duration;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.player_service.player_service.model.Player;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

// Annotation pour indiquer que cette classe est un contrôleur REST
@RestController
// Chemin de base pour toutes les méthodes de ce contrôleur
@RequestMapping("/players")
public class PlayerController {

    // Déclaration du circuit breaker
    private static final CircuitBreaker circuitBreaker;
    
	@Autowired
	RestTemplate restTemplate;

    public PlayerController(RestTemplateBuilder restTemplateBuilder) {
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
        circuitBreaker = CircuitBreaker.of("player-service", circuitBreakerConfig);
    }

    // Liste des équipes
    static ArrayList<Player> listePlayers = new ArrayList<Player>() {
        {
            add(new Player(1, "Jean", 10, 25));
            add(new Player(2, "François", 15, 30));
            add(new Player(3, "Dimitri", 20, 35));
            add(new Player(4, "Pilou", 25, 40));
            add(new Player(5, "Paul", 30, 45));
            add(new Player(6, "Jacques", 35, 50));
            add(new Player(7, "Pierre", 40, 55));
            add(new Player(8, "David", 45, 60));
            add(new Player(9, "Lucas", 50, 65));
            add(new Player(10, "Julien", 55, 70));
            add(new Player(11, "Nicolas", 60, 75));
            add(new Player(12, "Jérôme", 65, 80));
            add(new Player(13, "Guillaume", 70, 85));
            add(new Player(14, "Benoît", 75, 90));
            add(new Player(15, "Christophe", 80, 95));
            add(new Player(16, "Antoine", 85, 100));
            add(new Player(17, "Vincent", 90, 105));
            add(new Player(18, "Maxime", 95, 110));
            add(new Player(19, "Alexandre", 100, 115));
            add(new Player(20, "Olivier", 105, 120));
            add(new Player(21, "Thomas", 110, 125));
            add(new Player(22, "Robert", 115, 130));
            add(new Player(23, "Patrick", 120, 135));
            add(new Player(24, "Daniel", 125, 140));
            add(new Player(25, "Michel", 130, 145));
        }
    };

    // Méthode pour obtenir une équipe par son ID
    @GetMapping("/{id}")
    public ResponseEntity<String> getPlayer(@PathVariable int id) {

        try {
            // Utilisation du circuit breaker pour exécuter la méthode getPlayerById
            return circuitBreaker.executeSupplier(() -> getPlayerById(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Équipe non trouvée");
        }
    }

    // Méthode pour obtenir une équipe par son ID
    private ResponseEntity<String> getPlayerById(int id) {
        for (Player player : listePlayers) {
            if (player.getId() == id) {
                return new ResponseEntity<>(player.getName(), HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

    // Méthode pour créer une équipe
    @PostMapping
    public ResponseEntity<String> createPlayer(@RequestBody Player player) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode createAndAddPlayer
            return circuitBreaker.executeSupplier(() -> createAndAddPlayer(player));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Cette action n'a pas été faite");
        }
    }

    // Méthode pour créer une équipe et l'ajouter à la liste des équipes
    private ResponseEntity<String> createAndAddPlayer(Player player) {
        listePlayers.add(player);
        return new ResponseEntity<>("Équipe créée : " + player.getName(), HttpStatus.CREATED);
    }

    // Méthode pour mettre à jour une équipe existante
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePlayer(@PathVariable int id, @RequestBody Player player) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode updateExistingPlayer
            return circuitBreaker.executeSupplier(() -> updateExistingPlayer(id, player));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Pas pu mettre à jour");
        }
    }

    // Méthode pour mettre à jour une équipe existante
    private ResponseEntity<String> updateExistingPlayer(int id, Player player) {
        for (Player currentPlayer : listePlayers) {
            if (currentPlayer.getId() == id) {
                currentPlayer.setName(player.getName());
                return new ResponseEntity<String>(currentPlayer.getName(), HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

    // Méthode pour supprimer une équipe existante
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable int id) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode deleteExistingPlayer
            return circuitBreaker.executeSupplier(() -> deleteExistingPlayer(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Pas pu supprimé");
        }
    }

    // Méthode pour supprimer une équipe existante de la liste des équipes
    private ResponseEntity<String> deleteExistingPlayer(int id) {
        for (Player player : listePlayers) {
            if (player.getId() == id) {
                listePlayers.remove(player);
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
