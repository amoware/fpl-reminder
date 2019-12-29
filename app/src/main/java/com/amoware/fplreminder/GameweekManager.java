package com.amoware.fplreminder;

import java.util.List;

/**
 * Created by amoware on 2019-12-29.
 */
public class GameweekManager {

    private Gameweek currentGameweek;
    private List<Gameweek> gameweeks;
    private GameweekClient gameweekClient;


    public static void initialize(){
    }

    public Gameweek getCurrentGameweek() {
        return currentGameweek;
    }

    public static void refreshGameweek(){
    }

    public List readGameweeksFromDatabase(){
        return gameweeks;
    }

    public static void saveGameweeksToDatabase(Gameweek gameweeks){
    }
}

