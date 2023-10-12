package com.match_service.match_service.model;


public class Match {
public int matchId;
public String matchName;

    public Match(int id, String name){
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
