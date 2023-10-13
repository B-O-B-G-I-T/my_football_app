package com.team_service.team_service.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team_service.team_service.model.Team;

@RestController
public class TeamController {

    static ArrayList<Team> listeTeams = new ArrayList<Team>() {
        {
            add(new Team(1, "SCB"));
            add(new Team(2, "PSG"));
            add(new Team(3, "OM"));
            add(new Team(4, "ACA"));
        }
    };


    //@GetMapping("/{id}")
    //@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequestMapping(value = "/teams/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable int id) {
        for (Team team : listeTeams) {
            if (team.getId() == id) {
                return new ResponseEntity<>(team, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//     //@GetMapping("/{id}")
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//     public ResponseEntity<Team> getTeam(@PathVariable int id) {
//         for (Team team : listeTeams) {
//             if (team.getId() == id) {
//                 return new ResponseEntity<>(team, HttpStatus.OK);
//             }
//         }
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     }

    //@PostMapping
    @RequestMapping( method = RequestMethod.POST)
    public String createTeam(@RequestBody Map<String, String> teamMap) {
        Team team = new Team(Integer.parseInt(teamMap.get("teamId")), teamMap.get("teamName"));
        listeTeams.add(team);
        return "Équipe créée : " + team.getName();
    }

    //@PutMapping("/{id}")
   @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Team> updateTeam(@PathVariable int id, @RequestBody Team team) {
        for (Team currentTeam : listeTeams) {
            if (currentTeam.getId() == id) {
                currentTeam.setName(team.getName());
                return new ResponseEntity<>(currentTeam, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //@DeleteMapping("/{id}")
   @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTeam(@PathVariable int id) {
        for (Team team : listeTeams) {
            if (team.getId() == id) {
                listeTeams.remove(team);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
