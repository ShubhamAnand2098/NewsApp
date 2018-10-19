package com.example.a1605594.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView gmail;
    private CircleImageView facebook, whatsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        gmail = findViewById(R.id.email_image);
        facebook = findViewById(R.id.fb_image);
        whatsApp = findViewById(R.id.whatsapp_image);

        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + "7860302145");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(i);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fbIntent = new Intent(Intent.ACTION_VIEW);
                fbIntent.setData(Uri.parse("https://www.facebook.com/shubham.anand.11"));
                startActivity(fbIntent);
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmailIntent = new Intent(Intent.ACTION_SEND);
                gmailIntent.setType("text/plain");
                gmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"shubham1998.anand@gmail.com"});
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback regarding NewsApp");
                gmailIntent.putExtra(Intent.EXTRA_TEXT, "Give your Feedback here...");
                startActivity(gmailIntent);
            }
        });

    }
}
