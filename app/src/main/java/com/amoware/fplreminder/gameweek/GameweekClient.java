package com.amoware.fplreminder.gameweek;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.amoware.fplreminder.common.Constants.API_URL;

/**
 * Created by amoware on 2019-12-29.
 */
public class GameweekClient {

    private HttpClient httpClient;
    private SimpleDateFormat simpleDateFormat;

    public GameweekClient() {
        this.httpClient = new HttpClient();
    }

    public List<Gameweek> getGameweeksFromFPL() throws Exception {
        // Hämta api-datan
        String apiString = httpClient.sendGetRequest(API_URL);

        // Hämta ut gameweeks från api-datan
        return convertStringToGameweeks(apiString);
    }

    public List<Gameweek> convertStringToGameweeks(String gameweeksString) throws Exception {
        Locale locale = new Locale("en");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // 1. Läs in vår data i ett json-objekt (eftersom det e ett json-objekt)
        JSONObject jsonObject = new JSONObject(gameweeksString);

        // 2. Hämta ut objektets json-array baserat på propertyn "events"
        JSONArray jsonArray = jsonObject.getJSONArray("events");

        // for-loop som går igenom jsonArray och lägger in gameweek i en lista
        List<Gameweek> gameweekArrayList = new ArrayList<Gameweek>();
        for (int i = 0; i < jsonArray.length(); i++) {
            // 3. Hämta ut json-arrayens första objekt
            JSONObject jsonObject2  = (JSONObject) jsonArray.get(i);

            // 4. Utifrån första objektet, hämta ut värdet för dess property "name"
            String name = jsonObject2.getString("name");

            // 5. Hämta värdet från propertyn "deadline-time"
            String deadlineDate = jsonObject2.getString("deadline_time");

            // 6. Gör om strängen 2019-08-09T18:00:00Z till ett Date-objekt
            Date deadlineTime = convertStringToDate(deadlineDate);

            // 7. Lägg in name och deadlineTime i ett Gameweek-objekt
            Gameweek gameweek = new Gameweek(name, deadlineTime);

            // 8. Lägg till gameweek i listan
            gameweekArrayList.add(gameweek);
        }
        return gameweekArrayList;
    }

    private Date convertStringToDate(String stringDate) throws ParseException {
        return simpleDateFormat.parse(stringDate);
    }

}