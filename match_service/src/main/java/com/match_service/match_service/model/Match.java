package com.match_service.match_service.model;


public class Match {

    int matchId;
    String equipe1;
    String equipe2;

    public Match(int id, String equipe1, String equipe2) {
        matchId = id;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    }

    public int getId() {
        return matchId;
    }

    public String getName() {
        return equipe1 + "/" + equipe2;
    }

    public String getNameEquipe1() {
        return equipe1 ;
    }

    public void setNameEquipe1(String name) {
        equipe1 = name;
    }

    public String getNameEquipe2() {
        return equipe2 ;
    }
    public void setNameEquipe2(String name) {
        equipe2 = name;
    }

}
