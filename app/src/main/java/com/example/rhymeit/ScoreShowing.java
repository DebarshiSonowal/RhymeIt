package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.elasticviews.ElasticButton;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ScoreShowing extends AppCompatActivity {
CircleProgressBar mCircleProgressBar;
ElasticButton playagain;
TextView score,target;
ImageView whatsapp,facebook;
FirebaseFirestore db;
DocumentReference note;
int i,l;
Long money;
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
        db = FirebaseFirestore.getInstance();
        note = db.document("UserProfile/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        getWindow().setStatusBarColor(Color.parseColor("#383C46"));
        getWindow().setNavigationBarColor(Color.parseColor("#5784CF"));

        SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
        if (preferences != null) {
            i = preferences.getInt("score",0);
            l = preferences.getInt("level",1);
        }else{
            i = getIntent().getIntExtra("score",0);
            l = getIntent().getIntExtra("level",0);
        }
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
                intent.putExtra(Intent.EXTRA_TEXT,"Hey look my new high score: "+i+" on level "+l+". Can you beat my score ? \n"+"http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(intent.createChooser(intent,"Share it"));
            }
        });

        score = findViewById(R.id.score);
        score.setText(i+"");

    }

    @Override
    protected void onStart() {
        super.onStart();
        note.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                     money = documentSnapshot.getLong("Coin");
                    if(i==10){
                        Map<String, Object> score = new HashMap<>();
                        score.put("Coin",getPrize()+money);
                        note.update(score);
                    }
                }
            }
        });

    }

    private Integer getPrize() {
        Integer money;
        switch (l){
            case 2:
                money = 30;
                break;
            case 3:
                money = 40;
                break;
            case 4:
                money = 50;
                break;
            default:
                money = 20;
                break;
        }
        return money;
    }
}