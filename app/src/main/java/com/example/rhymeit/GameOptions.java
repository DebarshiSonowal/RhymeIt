package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class GameOptions extends AppCompatActivity {
LinearLayout level1,level2,level3,level4;
MaterialProgressBar level1bar,level2bar,level3bar,level4bar;
DocumentReference note;
String uid;
FirebaseFirestore db;

    @Override
    protected void onStart() {
        super.onStart();
        note.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(GameOptions.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d("Exception", e.toString());
                    return;
                }
                assert documentSnapshot != null;
                if(documentSnapshot.exists()){
                    level1bar.setProgress(Integer.valueOf(documentSnapshot.get("level1").toString()));
                    level2bar.setProgress(Integer.valueOf(documentSnapshot.get("level2").toString()));
                    level3bar.setProgress(Integer.valueOf(documentSnapshot.get("level3").toString()));
                    level4bar.setProgress(Integer.valueOf(documentSnapshot.get("level4").toString()));
                }else{
                    level1bar.setProgress(0);
                    level2bar.setProgress(0);
                    level3bar.setProgress(0);
                    level4bar.setProgress(0);
                }
            }
        });

//        SharedPreferences sharedPreferences = getSharedPreferences("progress",MODE_PRIVATE);
//        sharedPreferences.getInt("level1",0);
//        sharedPreferences.getInt("level2",0);
//        sharedPreferences.getInt("level3",0);
//        sharedPreferences.getInt("level4",0);
//
//        level1bar.setProgress(sharedPreferences.getInt("level1",0));
//        level2bar.setProgress(sharedPreferences.getInt("level2",0));
//        level3bar.setProgress(sharedPreferences.getInt("level3",0));
//        level4bar.setProgress(sharedPreferences.getInt("level4",0));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_options);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        note  = db.document("UserProfile/"+uid);
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


        //Max
        level1bar.setMax(20);
        level2bar.setMax(20);
        level3bar.setMax(20);
        level4bar.setMax(20);

        //Set Progress



        //Onclick
        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",1);
                startActivity(intent);
            }
        });
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",2);
                startActivity(intent);
            }
        });
        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",3);
                startActivity(intent);
            }
        });
        level4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",4);
                startActivity(intent);
            }
        });

    }
}