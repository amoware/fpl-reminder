package com.amoware.fplreminder.model.gameweek;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.util.Log;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amoware.fplreminder.common.DateUtil;
import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.model.async.AsyncTask;

/**
 * Created by amoware on 2020-02-11.
 */
public class FetchGameweeksTask extends AsyncTask<Void, List<Gameweek>> {
    @NonNull
    private final FplReminder mFplReminder;
    @NonNull
    private final GameweeksTaskInterface mGameweeksTaskInterface;

    private static final long SIX_HOURS_IN_MILLISECONDS = 21600000;

    public FetchGameweeksTask(@NonNull FplReminder fplReminder,
                              @NonNull GameweeksTaskInterface gameweeksTaskInterface) {
        mFplReminder = fplReminder;
        mGameweeksTaskInterface = gameweeksTaskInterface;
    }

    @Nullable
    @Override
    protected List<Gameweek> doInBackground(Void aVoid) {
        if (!isCacheExpired()) {
            Log.d(tagger(getClass()), "cached, getting gameweeks from storage");
            return mFplReminder.getGameweeksFromStorage();
        }

        GameweeksClient client = new GameweeksClient();
        List<Gameweek> gameweeks = client.fetchGameweeks();

        if (gameweeks != null && gameweeks.size() > 0) {
            mFplReminder.writeGameweeksToStorage(gameweeks);
            mFplReminder.updateFetchedGameweeksDate();
        }

        return gameweeks;
    }

    private boolean isCacheExpired() {
        Date date = mFplReminder.getFetchedGameweeksDate();
        return !DateUtil.isToday(date) || DateUtil.getMillisecondsAgo(date) >= SIX_HOURS_IN_MILLISECONDS;
    }

    @Override
    protected void onPostExecute(List<Gameweek> gameweeks) {
        mGameweeksTaskInterface.onGameweeksDownloaded(gameweeks);
    }
}
