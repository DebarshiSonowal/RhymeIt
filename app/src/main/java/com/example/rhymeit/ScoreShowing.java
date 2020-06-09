package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.skydoves.elasticviews.ElasticButton;

import java.net.URL;

public class ScoreShowing extends AppCompatActivity {
CircleProgressBar mCircleProgressBar;
ElasticButton playagain;
TextView score,target;
ImageView whatsapp,facebook;
FirebaseAuth mFirebaseAuth;
int i;
    public static final String name ="Progress";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ScoreShowing.this,Home.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_showing);
        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
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
                Intent intent = new Intent(ScoreShowing.this,GameOptions.class);
                startActivity(intent);
            }
        });

        //whatsapp
        whatsapp = findViewById(R.id.whatsappshare);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT,"Hey look my new high score: "+i+". Can you beat my score ? \n"+"http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(intent);

            }
        });

        //facebook
        facebook = findViewById(R.id.facebookshare);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Hey look my new high score: "+i+". Can you beat my score ? \n"+"http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(intent.createChooser(intent,"Share it"));
            }
        });

        score = findViewById(R.id.score);
        score.setText(i+"");

    }
}