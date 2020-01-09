package com.amoware.fplreminder;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by amoware on 2019-12-29.
 */
public class GameweekClient {

    private HttpClient httpClient;
    private final String API_DATA = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public static void main(String[] args) throws Exception {
        // Skapa ett GameweekClient-objekt
        getGameweeksFromFPL();
    }

    public List<Gameweek> getGameweeksFromFPL() throws Exception {
        // Hämta api-datan
        String apiString;
        apiString = httpClient.sendGetRequest(API_DATA);

        // Skapa en lista över gameweeks.
        // 1. Gör om apiString till ett JSONObject
        JSONObject jsonObject = new JSONObject(apiString);

        //Tvivlar..
        //jsonObject.getJSONArray("events");

        // 2. Json-objektet har fältet "events" som består av en array (JSONArray) med gameweeks. Hämta ut JSONArray:en "events"


        return null;
    }

    private List<Gameweek> stringToListConverter(String gameweeksString) {
        return null;
    }
}