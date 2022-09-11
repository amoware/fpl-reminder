package com.amoware.fplreminder.gameweek;

import android.util.Log;

import java.util.List;

import static com.amoware.fplreminder.common.Constants.API_URL;
import static com.amoware.fplreminder.common.Constants.tagger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amoware.fplreminder.common.GameweekParser;
import com.amoware.fplreminder.model.async.AsyncTask;

/**
 * Created by amoware on 2020-02-11.
 */
public class GameweeksTask extends AsyncTask<Void, List<Gameweek>> {
    @NonNull
    private final GameweeksTaskInterface mGameweeksTaskInterface;

    public GameweeksTask(@NonNull GameweeksTaskInterface gameweeksTaskInterface) {
        mGameweeksTaskInterface = gameweeksTaskInterface;
    }

    @Nullable
    @Override
    protected List<Gameweek> doInBackground(Void aVoid) {
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

        mGameweeksTaskInterface.writeBootstrapStaticContentToFile(strippedBootstrapStatic);
        return GameweekParser.toGameweeks(strippedBootstrapStatic);
    }

    @Override
    protected void onPostExecute(List<Gameweek> gameweeks) {
        mGameweeksTaskInterface.onGameweeksDownloaded(gameweeks);
    }
}
