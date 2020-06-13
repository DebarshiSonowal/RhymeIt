package com.example.rhymeit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonClickListener;
import com.skydoves.balloon.OnBalloonDismissListener;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class GameOptions extends AppCompatActivity {
LinearLayout level1,level2,level3,level4;
MaterialProgressBar level1bar,level2bar,level3bar,level4bar;
DocumentReference note;
String uid;
FirebaseFirestore db;
Balloon ballon,ballon2,ballon3,ballon4;
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
                    level1bar.setProgress(documentSnapshot.getLong("level1").intValue());
                    level2bar.setProgress(documentSnapshot.getLong("level2").intValue());
                    level3bar.setProgress(documentSnapshot.getLong("level3").intValue());
                    level4bar.setProgress(documentSnapshot.getLong("level4").intValue());
                }else{
                    level1bar.setProgress(0);
                    level2bar.setProgress(0);
                    level3bar.setProgress(0);
                    level4bar.setProgress(0);
                }
            }
        });
        //level1
        ballon = new Balloon.Builder(GameOptions.this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowVisible(true)
                .setWidthRatio(1.0f)
                .setHeight(65)
                .setTextSize(15f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("This is the first level. No requirement")
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setIconDrawable(ContextCompat.getDrawable(GameOptions.this, R.drawable.ic_info))
                .setBackgroundColor(Color.parseColor("#6A38A7"))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();
        ballon.setOnBalloonClickListener(new OnBalloonClickListener() {
            @Override
            public void onBalloonClick(@NotNull View view) {
                ballon.dismiss();
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",1);
                startActivity(intent);
            }
        });
//        ballon.setOnBalloonDismissListener(new OnBalloonDismissListener() {
//            @Override
//            public void onBalloonDismiss() {
//                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
//                intent.putExtra("level",1);
//                startActivity(intent);
//            }
//        });

        ballon2  = new Balloon.Builder(GameOptions.this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowVisible(true)
                .setWidthRatio(1.0f)
                .setHeight(65)
                .setTextSize(15f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("This is the second level. Minimum word length is 2")
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setIconDrawable(ContextCompat.getDrawable(GameOptions.this, R.drawable.ic_info))
                .setBackgroundColor(Color.parseColor("#A7383D"))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();
        ballon2.setOnBalloonClickListener(new OnBalloonClickListener() {
            @Override
            public void onBalloonClick(@NotNull View view) {
                ballon2.dismiss();
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",2);
                startActivity(intent);
            }
        });
//        ballon2.setOnBalloonDismissListener(new OnBalloonDismissListener() {
//            @Override
//            public void onBalloonDismiss() {
//                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
//                intent.putExtra("level",2);
//                startActivity(intent);
//            }
//        });

        ballon3  = new Balloon.Builder(GameOptions.this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowVisible(true)
                .setWidthRatio(1.0f)
                .setHeight(65)
                .setTextSize(15f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("This is the third level. Minimum word length is 3")
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setIconDrawable(ContextCompat.getDrawable(GameOptions.this, R.drawable.ic_info))
                .setBackgroundColor(Color.parseColor("#74A738"))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();
        ballon3.setOnBalloonClickListener(new OnBalloonClickListener() {
            @Override
            public void onBalloonClick(@NotNull View view) {
                ballon3.dismiss();
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",3);
                startActivity(intent);
            }
        });
        ballon4  = new Balloon.Builder(GameOptions.this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowVisible(true)
                .setWidthRatio(1.0f)
                .setHeight(65)
                .setTextSize(15f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("This is the forth level. Minimum word length is 4")
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setIconDrawable(ContextCompat.getDrawable(GameOptions.this, R.drawable.ic_info))
                .setBackgroundColor(Color.parseColor("#38A7A2"))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();
        ballon4.setOnBalloonClickListener(new OnBalloonClickListener() {
            @Override
            public void onBalloonClick(@NotNull View view) {
                ballon4.dismiss();
                Intent intent = new Intent(GameOptions.this,MainActivity2.class);
                intent.putExtra("level",4);
                startActivity(intent);
            }
        });
        //level2

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
        getWindow().setStatusBarColor(Color.parseColor("#350032"));
        getWindow().setNavigationBarColor(Color.parseColor("#008592"));
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
        level1bar.setMax(10);
        level2bar.setMax(10);
        level3bar.setMax(10);
        level4bar.setMax(10);

        //Set Progress



        //Onclick
        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ballon.show(level1,0,0);
            }
        });
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             ballon2.show(level2,0,0);
            }
        });
        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ballon3.show(level3,0,0);
            }
        });
        level4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ballon4.show(level4,0,0);
            }
        });

    }
}