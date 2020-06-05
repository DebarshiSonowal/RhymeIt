package com.example.rhymeit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.github.nikartm.button.FitButton;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class Home extends AppCompatActivity {
//pixel perfect
    //Ilya Pavlov
//NEW
//Convert to GIF
//
//Download
//AEP
//JSON
//
//
//Like
    ElasticImageView share,about,rating;
    FitButton rhymebtn;
    MaterialProgressBar mProgressBar;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.parseColor("#0F2027"));
        getWindow().setNavigationBarColor(Color.parseColor("#2C5364"));
        rhymebtn = findViewById(R.id.rhymebtn);
        rhymebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,GameOptions.class);
                startActivity(intent);
            }
        });

        mProgressBar = findViewById(R.id.gamebar);
        mProgressBar.setMax(100);

        mProgressBar.setProgress(20,true);

        //share
        share = findViewById(R.id.sharebtn);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_TEXT,"Hey look my new high score: "+20+". Can you beat my score ? \n"+"http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent1,"Share it"));
            }
        });
        about = findViewById(R.id.aboutbtn);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,About.class);
                startActivity(intent);
            }
        });

        rating = findViewById(R.id.ratingview);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });
    }
}
