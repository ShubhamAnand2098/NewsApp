package com.example.a1605594.newsapp;

public class NewsFeed {

    private String mPhotoUrl;
    private String mArticleTitle;
    private String mDescription;
    private String mReadMoreUrl;
    private String mSource;
    private String mAuthor;

    public NewsFeed(String photoUrl, String newsText, String description
            , String url, String source, String author) {
        mPhotoUrl = photoUrl;
        mArticleTitle = newsText;
        mDescription = description;
        mReadMoreUrl = url;
        mSource = source;
        mAuthor = author;
    }

    public String getmArticleTitle() {
        return mArticleTitle;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmReadMoreUrl() {
        return mReadMoreUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmSource() {
        return mSource;
    }
}
