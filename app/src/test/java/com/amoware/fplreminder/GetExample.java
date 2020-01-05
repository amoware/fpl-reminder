package com.amoware.fplreminder;

        import java.io.IOException;
        import java.net.URL;
        import java.util.concurrent.TimeUnit;

        import okhttp3.MediaType;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

// GET an URL
public class GetExample {
    public String url = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public String run(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
        return response.body().string();

        /*try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }*/
    }


    public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String response = example.run(example.url);
        System.out.println(response);
    }
}