package com.amoware.fplreminder;

import java.util.List;

public class GameweekManager {

    private Gameweek currentGaneweek;
    private List Gameweek;
    private GameweekClient gameweekClient;


    public static void Initialize(){
    }

    public Gameweek getCurrentGaneweek() {
        return currentGaneweek;
    }

    public static void refreshGameweek(){
    }

    public List readGameweeksFromDatabase(){
        return Gameweek;
    }

    public static void saveGameweeksToDatabase(Gameweek gameweeks){
    }
}

