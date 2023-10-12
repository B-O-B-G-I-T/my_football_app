package com.match_service.match_service.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.match_service.match_service.model.Match;

@RestController
@RequestMapping("/matchs")
public class MatchController {

    static ArrayList<Match> listeMatchs = new ArrayList<Match>() {
    {
            add(new Match(1, "scb/aca"));
            add(new Match(2, "jose"));
            add(new Match(3, "qq"));
            add(new Match(4, "il est toussaint"));
        }
    };

    @GetMapping("/{id}")
    public Match getMatch(@PathVariable int id) {
        Optional<Match> Match = listeMatchs.stream().filter(p -> p.getId() == id).findFirst();
        return Match.orElse(null);
    }

    @PostMapping
    public Match addMatch(@RequestBody Match newMatch) {
        listeMatchs.add(newMatch);
        return newMatch;
    }

    @PutMapping("/{id}")
    public Match updateMatch(@PathVariable int id, @RequestBody Match updatedMatch) {
        for (Match Match : listeMatchs) {
            if (Match.getId() == id) {
                Match.setName(updatedMatch.getName());
                return Match;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable int id) {
        listeMatchs.removeIf(Match -> Match.getId() == id);
    }
}
