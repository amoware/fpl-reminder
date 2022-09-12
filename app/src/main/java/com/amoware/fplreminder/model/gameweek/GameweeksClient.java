package com.amoware.fplreminder.model.gameweek;

import static com.amoware.fplreminder.common.Constants.API_URL;
import static com.amoware.fplreminder.common.Constants.tagger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amoware.fplreminder.common.GameweekParser;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.gameweek.HttpClient;

import java.util.List;

/**
 * Created by amoware on 2022-09-11.
 */
public class GameweeksClient {
    @NonNull
    private final HttpClient mHttpClient;

    public GameweeksClient() {
        mHttpClient = new HttpClient();
    }

    @Nullable
    public List<Gameweek> fetchGameweeks() {
        String bootstrapStatic = null;
        try {
            bootstrapStatic = mHttpClient.sendGetRequest(API_URL);
        } catch (Exception e) {
            Log.e(tagger(getClass()), "Exception", e);
        }

        return GameweekParser.toGameweeks(bootstrapStatic);
    }
}
