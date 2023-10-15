package com.stats_service.stats_service.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.stats_service.stats_service.model.Statistique;

@RestController
public class StatistiqueController {

    @Autowired
    RestTemplate restTemplate;

    public StatistiqueController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    static ArrayList<Statistique> listeStatistiques = new ArrayList<Statistique>() {
        {
            add(new Statistique(1, "scb/aca"));
            add(new Statistique(2, "jose"));
            add(new Statistique(3, "qq"));
            add(new Statistique(4, "il est toussaint"));
        }
    };

    @GetMapping("team-stats/{teamId}")
    public Statistique getStatistiqueByTeam(@PathVariable int id) {
        Optional<Statistique> Statistique = listeStatistiques.stream().filter(p -> p.getId() == id).findFirst();
        return Statistique.orElse(null);
    }

    @GetMapping("player-stats/{playerId}")
    public Statistique getStatistiqueByPlayer(@PathVariable int id) {
        Optional<Statistique> Statistique = listeStatistiques.stream().filter(p -> p.getId() == id).findFirst();
        return Statistique.orElse(null);
    }
}
