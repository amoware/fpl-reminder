package com.amoware.fplreminder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amoware on 2019-12-29.
 */
public class HttpClient {

    public static void main(String[] args) throws Exception {
        HttpClient httpClient = new HttpClient();
        System.out.println(httpClient.sendGetRequest("https://fantasy.premierleague.com/api/bootstrap-static/"));
    }

    public String sendGetRequest(String url) throws Exception {
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try {
            HttpURLConnection connection = createHttpURLConnection(url);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private HttpURLConnection createHttpURLConnection(String url) throws IOException {
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestMethod("GET");
        connection.setReadTimeout(15*1000);
        connection.connect();

        return connection;
    }
}
