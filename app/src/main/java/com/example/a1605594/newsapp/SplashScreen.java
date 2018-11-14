package com.example.a1605594.newsapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private TextView splashTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        splashTextView = findViewById(R.id.splash_title);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/sweet_hipster.ttf");
        splashTextView.setTypeface(typeface);

        SharedPreferences sharedPreferences = getSharedPreferences("USER INFO", MODE_PRIVATE);
        final boolean alreadyLoggedIn = sharedPreferences.getBoolean("LoggedIn", false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(alreadyLoggedIn)
                {
                    Intent intent = new Intent(SplashScreen.this, NewsFeedActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    ActivityOptions options = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,
                                splashTextView, getString(R.string.title_transition_name));
                    }
                    startActivity(intent, options.toBundle());
                    finish();
                }

            }
        }, 1000);
    }
}
