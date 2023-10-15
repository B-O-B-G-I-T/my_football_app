package com.player_service.player_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.player_service.player_service.model.Player;

@Service
public class PlayerService {


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    // Liste des joueur
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
    
    // Méthode pour obtenir une joueur par son ID
     public ResponseEntity<String> getPlayerById(int id) {
        for (Player player : listePlayers) {
            if (player.getId() == id) {
                return new ResponseEntity<>(player.getName(), HttpStatus.OK);
            }
        }
        throw new RuntimeException("Joueur non trouvée");
    }

    // Méthode pour obtenir une joueur par son ID
    public ResponseEntity<String> getStatsPlayerById(int id) {
        for (Player player : listePlayers) {
            if (player.getId() == id) {
                Map<String, Object> stats = new HashMap<>();
                stats.put("name", player.getName());
                stats.put("nombreDeMatchJoue", player.getNombreDeMatchJoue());
                stats.put("pointMarque", player.getPointMarque());

                ObjectMapper mapper = new ObjectMapper();
                String json;
                try {
                    json = mapper.writeValueAsString(stats);

                    return new ResponseEntity<>(json, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                     e.printStackTrace();
                    throw new RuntimeException("Nous avons pas réussit à écrire le JSON");
                   
                }

            }
        }
        throw new RuntimeException("Joueur non trouvé");
    }

    // Méthode pour créer une joueur et l'ajouter à la liste des joueur
    public ResponseEntity<String> createAndAddPlayer(Player player) {
        listePlayers.add(player);
        return new ResponseEntity<>("Joueur créée : " + player.getName(), HttpStatus.CREATED);
    }

    // Méthode pour mettre à jour une équipe existante
    public ResponseEntity<String> updateExistingPlayer(int id, Player player) {
        for (Player currentPlayer : listePlayers) {
            if (currentPlayer.getId() == id) {
                currentPlayer.setName(player.getName());
                return new ResponseEntity<String>(currentPlayer.getName(), HttpStatus.OK);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

    // Méthode pour supprimer une joueur existante de la liste des joueur
    public ResponseEntity<String> deleteExistingPlayer(int id) {
        for (Player player : listePlayers) {
            if (player.getId() == id) {
                listePlayers.remove(player);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        throw new RuntimeException("Équipe non trouvée");
    }

}
