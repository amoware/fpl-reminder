package com.amoware.fplreminder.gameweek;

import java.util.List;

/**
 * Created by amoware on 2019-12-29.
 */
public class GameweekManager {

    private Gameweek currentGameweek;
    private List<Gameweek> gameweeks;
    private GameweekClient gameweekClient;

    public void initialize() {
    }

    public Gameweek getCurrentGameweek() {
        return currentGameweek;
    }

    public void refreshGameweek() {
    }

    public List<Gameweek> readGameweeksFromDatabase() {
        return gameweeks;
    }

    public void saveGameweeksToDatabase(List<Gameweek> gameweeks) {
    }
}
