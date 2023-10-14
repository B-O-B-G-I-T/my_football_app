package com.team_service.team_service.controller;

import java.time.Duration;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team_service.team_service.model.Team;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private static final CircuitBreaker circuitBreaker;

    static {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowSize(2)
                .build();

        circuitBreaker = CircuitBreaker.of("team-service", circuitBreakerConfig);
    }
    static ArrayList<Team> listeTeams = new ArrayList<Team>() {
        {
            add(new Team(1, "SCB"));
            add(new Team(2, "PSG"));
            add(new Team(3, "OM"));
            add(new Team(4, "ACA"));
        }
    };

    @GetMapping("/{id}")
    public ResponseEntity<String> getTeam(@PathVariable int id) {

        try {
            return circuitBreaker.executeSupplier(() -> getTeamById(id));
        } catch (Exception e) {
            return fallback("Équipe non trouvée");
        }
    }

    private ResponseEntity<String> getTeamById(int id) {
        for (Team team : listeTeams) {
            if (team.getId() == id) {
                return new ResponseEntity<>(team.teamName, HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

    // @PostMapping
    // public String createTeam(@RequestBody Team team) {
    // listeTeams.add(team);
    // return "Équipe créée : " + team.getName();
    // }

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody Team team) {
        try {
            return circuitBreaker.executeSupplier(() -> createAndAddTeam(team));
        } catch (Exception e) {
            return fallback("Cette action n'a pas été faite");
        }
    }

    private ResponseEntity<String> createAndAddTeam(Team team) {
        listeTeams.add(team);
        return new ResponseEntity<>("Équipe créée : " + team.getName(), HttpStatus.CREATED);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<Team> updateTeam(@PathVariable int id, @RequestBody
    // Team team) {
    // for (Team currentTeam : listeTeams) {
    // if (currentTeam.getId() == id) {
    // currentTeam.setName(team.getName());
    // return new ResponseEntity<>(currentTeam, HttpStatus.OK);
    // }
    // }
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteTeam(@PathVariable int id) {
    // for (Team team : listeTeams) {
    // if (team.getId() == id) {
    // listeTeams.remove(team);
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }
    // }
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable int id, @RequestBody Team team) {
        try {
            return circuitBreaker.executeSupplier(() -> updateExistingTeam(id, team));
        } catch (Exception e) {
            return fallback("Pas pu mettre à jour");
        }
    }

    private ResponseEntity<String> updateExistingTeam(int id, Team team) {
        for (Team currentTeam : listeTeams) {
            if (currentTeam.getId() == id) {
                currentTeam.setName(team.getName());
                return new ResponseEntity<String>(currentTeam.teamName, HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable int id) {
        try {
            return circuitBreaker.executeSupplier(() -> deleteExistingTeam(id));
        } catch (Exception e) {
            return fallback("Pas pu supprimé");
        }
    }

    private ResponseEntity<String> deleteExistingTeam(int id) {
        for (Team team : listeTeams) {
            if (team.getId() == id) {
                listeTeams.remove(team);
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
