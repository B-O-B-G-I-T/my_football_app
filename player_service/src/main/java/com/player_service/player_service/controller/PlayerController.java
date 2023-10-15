package com.player_service.player_service.controller;

// Importation des packages nécessaires

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.player_service.player_service.model.Player;
import com.player_service.player_service.service.PlayerService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.swagger.v3.oas.annotations.Operation;


// Annotation pour indiquer que cette classe est un contrôleur REST
@RestController
// Chemin de base pour toutes les méthodes de ce contrôleur
@RequestMapping("/players")
public class PlayerController {
private  PlayerService playerService;
    // Déclaration du circuit breaker
    private static final CircuitBreaker circuitBreaker;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
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



    // Méthode pour obtenir une équipe par son ID
    @GetMapping("/{id}")
    @Operation(summary = "connaitre le joueur avec son id")
    public ResponseEntity<String> getPlayer(@PathVariable int id) {

        try {
            // Utilisation du circuit breaker pour exécuter la méthode getPlayerById
            return circuitBreaker.executeSupplier(() -> playerService.getPlayerById(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Joueur non trouvée");
        }
    }

    

    @GetMapping("/Stats/{id}")
    @Operation(summary = "connaitre les stats du joueur avec son id")
    public ResponseEntity<String> getStatsPlayer(@PathVariable int id) {

        try {
            // Utilisation du circuit breaker pour exécuter la méthode getPlayerById
            return circuitBreaker.executeSupplier(() -> playerService.getStatsPlayerById(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Joueur non trouvée");
        }
    }


    // Méthode pour créer une équipe
    @PostMapping
    @Operation(summary = "Créer un joueur")
    public ResponseEntity<String> createPlayer(@RequestBody Player player) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode createAndAddPlayer
            return circuitBreaker.executeSupplier(() -> playerService.createAndAddPlayer(player));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Cette action n'a pas été faite");
        }
    }

    
    // Méthode pour mettre à jour une équipe existante
    @PutMapping("/{id}")
    @Operation(summary = "Mets le joueur à jour avec son id")
    public ResponseEntity<String> updatePlayer(@PathVariable int id, @RequestBody Player player) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode updateExistingPlayer
            return circuitBreaker.executeSupplier(() -> playerService.updateExistingPlayer(id, player));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Pas pu mettre à jour");
        }
    }

    

    // Méthode pour supprimer une équipe existante
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime le joueur avec son id")
    public ResponseEntity<String> deletePlayer(@PathVariable int id) {
        try {
            // Utilisation du circuit breaker pour exécuter la méthode deleteExistingPlayer
            return circuitBreaker.executeSupplier(() -> playerService.deleteExistingPlayer(id));
        } catch (Exception e) {
            // En cas d'erreur, on appelle la méthode fallback
            return fallback("Pas pu supprimé");
        }
    }

    // Méthode de repli en cas d'erreur
    private ResponseEntity<String> fallback(String mess) {
        // Vous pouvez personnaliser cette réponse en fonction de vos besoins
        return new ResponseEntity<>(mess, HttpStatus.NOT_FOUND);
    }

}
