package com.player_service.player_service.controller;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;

import com.player_service.player_service.model.Player;
import java.util.Optional;
@RestController
@RequestMapping("/players")
public class PlayerController {

      static ArrayList<Player> listePlayers = new ArrayList<Player>() {
        {
            add(new Player(1, "jean"));
            add(new Player(2, "jose"));
            add(new Player(3, "qq"));
            add(new Player(4, "il est toussaint"));
        }
    };

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable int id) {
        Optional<Player> player = listePlayers.stream().filter(p -> p.getId() == id).findFirst();
        return player.orElse(null);
    }

    @PostMapping
    public Player addPlayer(@RequestBody Player newPlayer) {
        listePlayers.add(newPlayer);
        return newPlayer;
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable int id, @RequestBody Player updatedPlayer) {
        for (Player player : listePlayers) {
            if (player.getId() == id) {
                player.setName(updatedPlayer.getName());
                return player;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable int id) {
        listePlayers.removeIf(player -> player.getId() == id);
    }
}
