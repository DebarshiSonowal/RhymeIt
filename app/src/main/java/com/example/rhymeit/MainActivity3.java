package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.skydoves.elasticviews.ElasticButton;

public class MainActivity3 extends AppCompatActivity {
CircleProgressBar mCircleProgressBar;
ElasticButton playagain;
ImageView whatsapp,facebook;
int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        SharedPreferences preferences = getSharedPreferences("Progress", Context.MODE_PRIVATE);
        i = preferences.getInt("score",0);

        //circlebar
        mCircleProgressBar = findViewById(R.id.line_progress);
        mCircleProgressBar.setMax(10);
        mCircleProgressBar.setProgress(i);

        //playagain
        playagain = findViewById(R.id.playagainbtn);
        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //whatsapp
        whatsapp = findViewById(R.id.whatsappshare);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //facebook
        facebook = findViewById(R.id.facebookshare);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}