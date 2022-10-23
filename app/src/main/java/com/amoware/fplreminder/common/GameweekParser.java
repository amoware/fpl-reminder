package com.amoware.fplreminder.common;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.util.Log;

import androidx.annotation.Nullable;

import com.amoware.fplreminder.gameweek.Gameweek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Jonas Eiselt on 2022-09-06.
 */
public class GameweekParser {
    private static final SimpleDateFormat simpleDateFormat;

    private static final String NAME_KEY = "name";
    private static final String DEADLINE_TIME_KEY = "deadline_time";
    private static final String EVENTS_KEY = "events";

    static {
        Locale locale = new Locale("en");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Nullable
    public static String toString(@Nullable List<Gameweek> gameweeks) {
        if (gameweeks == null || gameweeks.size() == 0) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (Gameweek gameweek : gameweeks) {
            JSONObject gameweekObject = null;
            try {
                gameweekObject = toJSONObject(gameweek);
            } catch (JSONException e) {
                Log.e(tagger(GameweekParser.class), "JSONException", e);
            }

            if (gameweekObject == null) {
                continue;
            }
            jsonArray.put(gameweekObject);
        }

        try {
            jsonObject.put(EVENTS_KEY, jsonArray);
        } catch (JSONException e) {
            Log.e(tagger(GameweekParser.class), "JSONException", e);
        }

        return jsonObject.toString();
    }

    @Nullable
    private static JSONObject toJSONObject(@Nullable Gameweek gameweek) throws JSONException {
        if (Gameweek.isEmpty(gameweek)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NAME_KEY, gameweek.getName());
        jsonObject.put(DEADLINE_TIME_KEY, simpleDateFormat.format(gameweek.getDeadlineTime()));
        return jsonObject;
    }

    @Nullable
    public static List<Gameweek> toGameweeks(@Nullable String content) {
        if (StringUtil.isStringEmpty(content)) {
            return null;
        }

        // 1. Läs in vår data i ett json-objekt (eftersom det e ett json-objekt)
        JSONObject jsonObject = getNullableJSONObject(content);
        if (jsonObject == null) {
            return null;
        }

        // 2. Hämta ut objektets json-array baserat på propertyn "events"
        JSONArray jsonArray = getNullableEventsArray(jsonObject);
        if (jsonArray == null) {
            return null;
        }

        // for-loop som går igenom jsonArray och lägger in gameweek i en lista
        List<Gameweek> gameweekArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            // 3. Hämta ut json-arrayens första objekt
            JSONObject eventJsonObject = getNullableJSONObjectFromArray(jsonArray, i);
            if (eventJsonObject == null) {
                continue;
            }

            // 4. Utifrån första objektet, hämta ut värdet för dess property "name"
            String name = getNullableString(eventJsonObject, NAME_KEY);

            // 5. Hämta värdet från propertyn "deadline-time"
            String deadlineDate = getNullableString(eventJsonObject, DEADLINE_TIME_KEY);
            if (deadlineDate == null) {
                continue;
            }

            // 6. Gör om strängen 2019-08-09T18:00:00Z till ett Date-objekt
            Date deadlineTime = convertStringToDate(deadlineDate);
            if (deadlineTime == null) {
                continue;
            }

            // 7. Lägg in name och deadlineTime i ett Gameweek-objekt
            Gameweek gameweek = new Gameweek(name, deadlineTime);

            // 8. Lägg till gameweek i listan
            gameweekArrayList.add(gameweek);
        }
        return gameweekArrayList;
    }

    private static String getNullableString(JSONObject jsonObject, String property) {
        try {
            return jsonObject.getString(property);
        } catch (JSONException e) {
            Log.e(tagger(GameweekParser.class), "JSONException", e);
        }
        return null;
    }

    @Nullable
    private static JSONObject getNullableJSONObjectFromArray(JSONArray jsonArray, int index) {
        try {
            return (JSONObject) jsonArray.get(index);
        } catch (JSONException e) {
            Log.e(tagger(GameweekParser.class), "JSONException", e);
        }
        return null;
    }

    @Nullable
    private static JSONArray getNullableEventsArray(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONArray(EVENTS_KEY);
        } catch (JSONException e) {
            Log.e(tagger(GameweekParser.class), "JSONException", e);
        }
        return null;
    }

    @Nullable
    private static JSONObject getNullableJSONObject(String value) {
        try {
            return new JSONObject(value);
        } catch (JSONException e) {
            Log.e(tagger(GameweekParser.class), "JSONException", e);
        }
        return null;
    }

    private static Date convertStringToDate(String stringDate) {
        try {
            return simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            Log.e(tagger(GameweekParser.class), "ParseException", e);
        }
        return null;
    }
}
