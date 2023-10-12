package com.player_service.player_service.model;

public class Player {
public int playerId;
public String playerName;

    public Player(int id, String name){
        playerId = id;
        playerName = name;
    }

    public int getId() {
        return playerId;
    }

    public String getName() {
        return playerName;
    }

    public void setName(String name) {
        playerName = name;
    }

}
