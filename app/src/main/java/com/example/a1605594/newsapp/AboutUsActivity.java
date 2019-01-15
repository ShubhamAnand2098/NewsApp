package com.example.a1605594.newsapp;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageView gmail = findViewById(R.id.email_image);
        CircleImageView facebook = findViewById(R.id.fb_image);
        CircleImageView whatsApp = findViewById(R.id.whatsapp_image);

        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formattedNumber = "917860302145";
                try{
                    Intent sendIntent =new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT,"");
                    sendIntent.putExtra("jid", formattedNumber +"@s.whatsapp.net");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
                catch(Exception e)
                {
                    Toast.makeText(AboutUsActivity.this,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
                }
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
