package com.amoware.fplreminder;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by amoware on 2020-02-11.
 */
public class GameweeksTask extends AsyncTask<Void,Void,List<Gameweek>> {

    private GameweeksTaskInterface gInterface;

    GameweeksTask(GameweeksTaskInterface gInterface) {
        this.gInterface = gInterface;
    }

    @Override
    protected List<Gameweek> doInBackground(Void... voids) {
        GameweekClient gameweekClient = new GameweekClient();
        List<Gameweek> gameweeks = null;
        try {
            gameweeks = gameweekClient.getGameweeksFromFPL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameweeks;
    }

    @Override
    protected void onPostExecute(List<Gameweek> gameweeks) {
        super.onPostExecute(gameweeks);
        gInterface.onGameweeksDownloaded(gameweeks);
    }
}
