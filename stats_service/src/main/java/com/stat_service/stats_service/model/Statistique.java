package com.stat_service.stats_service.model;

public class Statistique {
public int matchId;
public String matchName;

    public Statistique(int id, String name){
        matchId = id;
        matchName = name;
    }

    public int getId() {
        return matchId;
    }

    public String getName() {
        return matchName;
    }

    public void setName(String name) {
        matchName = name;
    }

}
