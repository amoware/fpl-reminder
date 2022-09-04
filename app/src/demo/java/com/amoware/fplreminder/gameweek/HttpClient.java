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
                "      \"deadline_time\": \"2020-06-21T11:37:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 2\",\n" +
                "      \"deadline_time\": \"2020-06-21T11:39:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 3\",\n" +
                "      \"deadline_time\": \"2020-06-21T11:41:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 4\",\n" +
                "      \"deadline_time\": \"2020-06-21T11:42:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gameweek 5\",\n" +
                "      \"deadline_time\": \"2023-06-21T11:44:00Z\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
