package com.amoware.fplreminder.gameweek;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2020-02-11.
 */
public class GameweeksTask extends AsyncTask<Void,Void,List<Gameweek>> {

    private GameweeksTaskInterface gInterface;

    public GameweeksTask(GameweeksTaskInterface gInterface) {
        this.gInterface = gInterface;
    }

    @Override
    protected List<Gameweek> doInBackground(Void... voids) {
        GameweekClient gameweekClient = new GameweekClient();
        List<Gameweek> gameweeks = null;
        try {
            gameweeks = gameweekClient.getGameweeksFromFPL();
        } catch (Exception e) {
            Log.e(tagger(getClass()), "Exception", e);
        }
        return gameweeks;
    }

    @Override
    protected void onPostExecute(List<Gameweek> gameweeks) {
        super.onPostExecute(gameweeks);
        gInterface.onGameweeksDownloaded(gameweeks);
    }
}
