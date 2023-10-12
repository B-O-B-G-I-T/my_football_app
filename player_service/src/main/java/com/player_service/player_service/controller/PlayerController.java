
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
@EnableEurekaClient
public class PlayerService {

      static ArrayList<Player> listePlayers = new ArrayList<Player>() {
        {
            add(new Player(1, "SCB"));
            add(new Player(2, "PSG"));
            add(new Player(3, "OM"));
            add(new Player(4, "ACA"));
        }
    };

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Player addPlayer(@RequestBody Player newPlayer) {
        return playerRepository.save(newPlayer);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return playerRepository.findById(id)
                .map(player -> {
                    player.setName(updatedPlayer.getName());
                    player.setTeam(updatedPlayer.getTeam());
                    return playerRepository.save(player);
                })
                .orElseGet(() -> {
                    updatedPlayer.setId(id);
                    return playerRepository.save(updatedPlayer);
                });
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
    }
}
