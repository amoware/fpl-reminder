package com.amoware.fplreminder.gameweek;

/**
 * Created by amoware on 2020-06-07.
 */
@SuppressWarnings("WeakerAccess")
public class HttpClient {
    @SuppressWarnings({"RedundantThrows", "unused"})
    public String sendGetRequest(String url) throws Exception {
        return "{\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 1\",\n" +
                "      \"deadline_time\": \"2022-10-22T11:04:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 2\",\n" +
                "      \"deadline_time\": \"2022-10-22T11:08:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 3\",\n" +
                "      \"deadline_time\": \"2022-10-22T11:12:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 4\",\n" +
                "      \"deadline_time\": \"2022-10-22T11:16:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 5\",\n" +
                "      \"deadline_time\": \"2023-06-07T12:59:00Z\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"shouldIgnoreMe\": \"Hello world!\",\n" +
                "  \"shouldIgnoreMeTo\": [\"Hello world!\"]\n" +
                "}";
    }
}
