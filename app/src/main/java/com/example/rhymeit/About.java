package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.skydoves.elasticviews.ElasticImageView;

public class About extends AppCompatActivity {
ElasticImageView bug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setStatusBarColor(Color.parseColor("#3B0035"));
        getWindow().setNavigationBarColor(Color.parseColor("#9D1432"));
        bug = findViewById(R.id.bug2);
        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("debarkhisonowal@gmail.com") +
                        "?subject=" + Uri.encode("Reporting a bug") +
                        "&body=" + Uri.encode("");
                Uri uri = Uri.parse(uriText);
                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));
            }
        });
    }
}