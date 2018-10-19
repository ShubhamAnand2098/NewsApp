package com.example.a1605594.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeed>> {

    private String mUrl;

    public NewsFeedLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<NewsFeed> loadInBackground() {
        String jsonResponse = QueryUtils.fetchNewsData(mUrl);

        return QueryUtils.extractNews(jsonResponse);
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }


}
