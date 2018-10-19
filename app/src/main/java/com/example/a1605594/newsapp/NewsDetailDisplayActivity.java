package com.example.a1605594.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class NewsDetailDisplayActivity extends AppCompatActivity {

    private GifImageView newsDetailImageView;
    private TextView newsDetailTextView;
    private TextView readMoreTextView;
    private TextView newsDetailTitle;
    private TextView sourceAuthorTextView;
    private Intent intent;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_display);

        newsDetailImageView = findViewById(R.id.news_detail_image_view);
        newsDetailTextView = findViewById(R.id.news_detail_text_view);
        newsDetailTitle = findViewById(R.id.news_detail_title_text_view);
        readMoreTextView = findViewById(R.id.read_more_text);
        sourceAuthorTextView = findViewById(R.id.source_author);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailDisplayActivity.super.onBackPressed();
            }
        });

        newsDetailImageView.setImageResource(R.drawable.loading_image);

        intent = getIntent();
        String imageToLoad = intent.getStringExtra("Image");

        /*
        One of the correct ways
        ====================================================

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("bmp");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ========================================================

        */

        SharedPreferences sharedPreferences = getSharedPreferences("My Shared", MODE_PRIVATE);
        String encoded = sharedPreferences.getString("bmp", null);

        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        newsDetailImageView.setImageBitmap(decodedByte);

        SharedPreferences.Editor editor = getSharedPreferences("My Shared", Context.MODE_PRIVATE).edit();
        editor.putString("bmp", null);
        editor.apply();

/*
        newsDetailImageView.setBackgroundResource(R.drawable.loading_image);

        Glide.with(newsDetailImageView.getContext())
                .load(imageToLoad)
                .centerCrop()
                .error(R.drawable.image_not_found)
                .into(newsDetailImageView);
*/
        newsDetailTitle.setText(intent.getStringExtra("Title"));
        newsDetailTextView.setText(intent.getStringExtra("Description"));

        readMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = intent.getStringExtra("Read More");
                Intent i = new Intent(NewsDetailDisplayActivity.this, WebDisplayActivity.class);
                i.putExtra("URL", url);
                startActivity(i);
            }
        });

        String sources = String.format(getResources().getString(R.string.source),
                intent.getStringExtra("Source"),
                intent.getStringExtra("Author"));
        sourceAuthorTextView.setText(sources);

    }
}

