package com.example.a1605594.newsapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements onRegisterClicked {

    private LinearLayout loginContainer;
    private ViewPager mViewPager;
    private TextView titleTextView;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.view_pager);
        titleTextView = findViewById(R.id.app_title);
        loginContainer = findViewById(R.id.login_container);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/sweet_hipster.ttf");
        titleTextView.setTypeface(customFont);

        CategoryAdapter categoryAdapter = new CategoryAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(categoryAdapter);


        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginContainer.setVisibility(View.VISIBLE);
            }
        }, 500);


    }


    @Override
    public void slideFragment() {
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
