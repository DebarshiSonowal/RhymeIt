package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.nikartm.button.FitButton;
import com.skydoves.elasticviews.ElasticButton;

public class Home extends AppCompatActivity {
//pixel perfect
    FitButton rhymebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rhymebtn = findViewById(R.id.rhymebtn);
        rhymebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,GameOptions.class);
                startActivity(intent);
            }
        });

    }
}
