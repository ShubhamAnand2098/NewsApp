package com.example.a1605594.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private QueryUtils() {

    }

    protected static String fetchNewsData(String mUrl) {
        URL url = createUrl(mUrl);
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String json = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(2000);
            httpURLConnection.setConnectTimeout(2500);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                json = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();

            if (inputStream != null)
                inputStream.close();
        }

        return json;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder jsonResponse = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                jsonResponse.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse.toString();

    }

    private static URL createUrl(String mUrl) {
        URL url = null;
        if (mUrl != null) {
            try {
                url = new URL(mUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return url;
    }

    protected static List<NewsFeed> extractNews(String jsonResponse) {
        ArrayList<NewsFeed> newsFeedArrayList = new ArrayList<>();
        try {
            JSONObject baseResponse = new JSONObject(jsonResponse);

            JSONArray articlesArray = baseResponse.getJSONArray("articles");
            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject currentArticle = articlesArray.getJSONObject(i);
                JSONObject source = currentArticle.getJSONObject("source");
                String sourceName = source.getString("name");
                String articleTitle = currentArticle.getString("title");
                String articleAuthor = currentArticle.getString("author");
                String description = currentArticle.getString("description");
                String url = currentArticle.getString("url");
                String imageUrl = currentArticle.getString("urlToImage");
                //String content = currentArticle.getString("content");

                newsFeedArrayList.add(new NewsFeed(imageUrl, articleTitle, description
                        , url, sourceName, articleAuthor));


            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("NewsFeedActivity", "Extracting Error");
        }

        return newsFeedArrayList;

    }
}
