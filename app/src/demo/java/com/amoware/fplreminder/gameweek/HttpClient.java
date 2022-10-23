package com.amoware.fplreminder.gameweek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by amoware on 2020-06-07.
 */
public class HttpClient {
    @SuppressWarnings({"RedundantThrows", "unused"})
    public String sendGetRequest(String url) throws Exception {
        String startTime = "2022-10-23T09:56:00Z";
        return "{\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 1\",\n" +
                "      \"deadline_time\": \"" + startTime + "\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 2\",\n" +
                "      \"deadline_time\": \"" + addMinutes(startTime, 2) + "\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 3\",\n" +
                "      \"deadline_time\": \"" + addMinutes(startTime, 4) + "\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 4\",\n" +
                "      \"deadline_time\": \"" + addMinutes(startTime, 6) + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"shouldIgnoreMe\": \"Hello world!\",\n" +
                "  \"shouldIgnoreMeToo\": [\"Hello world!\"]\n" +
                "}";
    }

    private String addMinutes(String time, int minutes) {
        SimpleDateFormat simpleDateFormat = null;
        try {
            Locale locale = new Locale("sv");
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (simpleDateFormat == null) {
            return null;
        }

        Date parsedDate = null;
        try {
            parsedDate = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedDate == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        calendar.add(Calendar.MINUTE, minutes);

        return simpleDateFormat.format(calendar.getTime());
    }
}
