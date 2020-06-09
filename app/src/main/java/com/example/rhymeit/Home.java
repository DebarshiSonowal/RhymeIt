package com.example.rhymeit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;

import javax.annotation.Nullable;

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
    FirebaseAuth mFirebaseAuth;
    ElasticImageView share,about,rating;
    FitButton rhymebtn,logout;
    TextView userid,username,score,progress;
    MaterialProgressBar mProgressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef;
    Integer i;
    @Override
    protected void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(Home.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d("Exception", e.toString());
                    return;
                }
                assert documentSnapshot != null;
                if(documentSnapshot.exists()){
                    username.setText(documentSnapshot.getString("Username"));
                 i =Integer.parseInt(documentSnapshot.get("level1").toString())+Integer.parseInt(documentSnapshot.get("level2").toString())
                            +Integer.parseInt(documentSnapshot.get("level3").toString())+Integer.parseInt(documentSnapshot.get("level4").toString());
                 Log.d("Score",i+"");
                    mProgressBar.setProgress(i/40,true);
                    Long a = i.longValue()/40;
                    a = a*100;
                    progress.setText(a+"%");

                }else{

                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.parseColor("#0F2027"));
        getWindow().setNavigationBarColor(Color.parseColor("#2C5364"));
        rhymebtn = findViewById(R.id.rhymebtn);
        progress = findViewById(R.id.progress);
        rhymebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,GameOptions.class);
                startActivity(intent);
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getCurrentUser().getUid();

        logout = findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,login.class));
            }
        });
        userid = findViewById(R.id.userid);
        username = findViewById(R.id.username);
        score = findViewById(R.id.progress);
        userid.setText(uid);
        noteRef  = db.document("UserProfile/"+uid);
//        noteRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
//                            username.setText(documentSnapshot.getString("Username"));
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

        mProgressBar = findViewById(R.id.gamebar);
        mProgressBar.setMax(100);

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
