package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class GameOptions extends AppCompatActivity {
LinearLayout level1,level2,level3,level4;
MaterialProgressBar level1bar,level2bar,level3bar,level4bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_options);

        //Linear layout
        level1 = findViewById(R.id.level1);
        level2 = findViewById(R.id.level2);
        level3 = findViewById(R.id.level3);
        level4 = findViewById(R.id.level4);

        //Progressbar
        level1bar = findViewById(R.id.level1bar);
        level2bar = findViewById(R.id.level2bar);
        level3bar = findViewById(R.id.level3bar);
        level4bar = findViewById(R.id.level4bar);

        //getdata
        SharedPreferences sharedPreferences = getSharedPreferences("progress",MODE_PRIVATE);
        sharedPreferences.getInt("level1",0);
        sharedPreferences.getInt("level2",0);
        sharedPreferences.getInt("level3",0);
        sharedPreferences.getInt("level4",0);

        //Max
        level1bar.setMax(20);
        level2bar.setMax(20);
        level3bar.setMax(20);
        level4bar.setMax(20);

        //Set Progress
        level1bar.setProgress(sharedPreferences.getInt("level1",0));
        level2bar.setProgress(sharedPreferences.getInt("level2",0));
        level3bar.setProgress(sharedPreferences.getInt("level3",0));
        level4bar.setProgress(sharedPreferences.getInt("level4",0));


        //Onclick
        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,RhymingGame.class);
                intent.putExtra("level",1);
                startActivity(intent);
            }
        });
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,RhymingGame.class);
                intent.putExtra("level",2);
                startActivity(intent);
            }
        });
        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,RhymingGame.class);
                intent.putExtra("level",3);
                startActivity(intent);
            }
        });
        level4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,RhymingGame.class);
                intent.putExtra("level",4);
                startActivity(intent);
            }
        });

    }
}