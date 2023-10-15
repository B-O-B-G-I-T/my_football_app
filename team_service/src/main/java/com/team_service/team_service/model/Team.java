package com.team_service.team_service.model;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Team")
public class Team {

    int teamId;
    String teamName;
    int nombreDeMatchJoue;
    int pointMarque;


    public Team(int id, String name, int nombreDeMatchJoue, int pointMarque) {
        teamId = id;
        teamName = name;
        this.nombreDeMatchJoue = nombreDeMatchJoue;
        this.pointMarque = pointMarque;
    }

    public int getId() {
        return teamId;
    }

    public String getName() {
        return teamName;
    }

    public void setName(String name) {
        teamName = name;
    }

    public int getNombreDeMatchJoue() {
        return nombreDeMatchJoue;
    }

    public int getPointMarque() {
        return pointMarque;
    }

}
