package com.team_service.team_service.model;


public class Team {
public int teamId;
public String teamName;

    public Team(int id, String name){
        teamId = id;
        teamName = name;
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

}
