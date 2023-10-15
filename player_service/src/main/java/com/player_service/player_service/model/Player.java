package com.player_service.player_service.model;


public class Player {

    int playerId;
    String playerName;
    int pointMarque;
    int nombreDeMatchJoue;

// constructeur
    public Player(int id, String name, int pointMarque, int nombreDeMatchJoue) {
        playerId = id;
        playerName = name;
        this.pointMarque = pointMarque;
        this.nombreDeMatchJoue = nombreDeMatchJoue;
    }

// GETTER
    public int getId() {
        return playerId;
    }

    public String getName() {
        return playerName;
    }

    public int getPointMarque() {
        return pointMarque;
    }

    public int getNombreDeMatchJoue() {
        return nombreDeMatchJoue;
    }

// SETTER
    public void setName(String name) {
        playerName = name;
    }

    public void setPointMarque(int pointMarque) {
        this.pointMarque = pointMarque;
    }


    public void setNombreDeMatchJoue(int nombreDeMatchJoue) {
        this.nombreDeMatchJoue = nombreDeMatchJoue;
    }

}
