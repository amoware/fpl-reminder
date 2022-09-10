package com.amoware.fplreminder.gameweek;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import static com.amoware.fplreminder.common.Constants.API_URL;
import static com.amoware.fplreminder.common.Constants.tagger;

import androidx.annotation.Nullable;

import com.amoware.fplreminder.common.GameweekParser;

/**
 * Created by amoware on 2020-02-11.
 */
public class GameweeksTask extends AsyncTask<Void,Void,List<Gameweek>> {
    private final GameweeksTaskInterface gInterface;

    public GameweeksTask(GameweeksTaskInterface gInterface) {
        this.gInterface = gInterface;
    }

    @Nullable
    @Override
    protected List<Gameweek> doInBackground(Void... voids) {
        HttpClient httpClient = new HttpClient();

        String bootstrapStatic = null;
        try {
            bootstrapStatic = httpClient.sendGetRequest(API_URL);
        } catch (Exception e) {
            Log.e(tagger(getClass()), "Exception", e);
        }

        String strippedBootstrapStatic = GameweekParser.stripAllButEvents(bootstrapStatic);
        if (strippedBootstrapStatic == null) {
            return null;
        }

        gInterface.writeBootstrapStaticContentToFile(strippedBootstrapStatic);
        return GameweekParser.toGameweeks(strippedBootstrapStatic);
    }

    @Override
    protected void onPostExecute(List<Gameweek> gameweeks) {
        super.onPostExecute(gameweeks);
        gInterface.onGameweeksDownloaded(gameweeks);
    }
}
