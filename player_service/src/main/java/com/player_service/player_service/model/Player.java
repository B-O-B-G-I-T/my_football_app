package com.player_service.player_service.model;


public class Player {

    int playerId;
    String playerName;
    int pointMarque;
    int nombreDeMatchJoue;
    

    public Player(int id, String name) {
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
