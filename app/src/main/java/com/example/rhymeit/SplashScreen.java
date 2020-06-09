package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
TextView tite,tag,from,publisher;
Animation top,bottom;
    int SPLASH_TIME = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tite = findViewById(R.id.title);
        tag = findViewById(R.id.tag);
        from = findViewById(R.id.fromview);
        publisher = findViewById(R.id.owner);

        getWindow().setStatusBarColor(Color.parseColor("#23074d"));
        getWindow().setNavigationBarColor(Color.parseColor("#cc5333"));
        top = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.top_animation );
        bottom = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.bottom_animation );
        tite.setAnimation(top);
        tag.setAnimation(bottom);
        from.setAnimation(bottom);
        publisher.setAnimation(bottom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this,login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME);
    }
}