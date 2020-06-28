package com.example.rhymeit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nikartm.button.FitButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.skydoves.elasticviews.ElasticImageView;

import javax.annotation.Nullable;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class Home extends AppCompatActivity {
    private static final String CHANNEL_ID ="exampleChannel" ;
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
    //Mr.Futuristic
    //Tu Nguyen
    FirebaseAuth mFirebaseAuth;
    ElasticImageView share,about,rating;
    FitButton rhymebtn,logout;
    TextView userid,username,score,progress,coin;
    MaterialProgressBar mProgressBar;
    private FirebaseFirestore db;
    private DocumentReference noteRef;
    boolean isConnected;
    double i;
    Long l1;
    Long l2;
    Long l3;
    Long l4;
    ProgressBar game;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(Home.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d("Exception", e.toString());
                    return;
                }
                if(documentSnapshot.exists()){
                    username.setText(documentSnapshot.getString("Username"));
                    Long b = documentSnapshot.getLong("Coin");
                    coin.setText(documentSnapshot.getLong("Coin")+"");
                    Log.d("Coin",b+"");
                    i =  calculate(documentSnapshot.getLong("level1"),documentSnapshot.getLong("level2"),documentSnapshot.getLong("level3"),documentSnapshot.getLong("level4"));
                    updateProgress((long) calculate(documentSnapshot.getLong("level1"),documentSnapshot.getLong("level2"),documentSnapshot.getLong("level3"),documentSnapshot.getLong("level4")));
                    l1 = documentSnapshot.getLong("level1");
                    l2 = documentSnapshot.getLong("level2");
                    l3 = documentSnapshot.getLong("level3");
                    l4 = documentSnapshot.getLong("level4");
                    Log.d("Progress3", i+"");
                }else{
                }
            }
        });
    userid.setOnClickListener(v -> {
        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData mdata = ClipData.newPlainText("practice",userid.getText());
        manager.setPrimaryClip(mdata);
    });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateProgress(Long calculate) {
        progress.setText(calculate+"%");
        mProgressBar.setProgress(calculate.intValue(),true);
        Log.d("Prog",calculate+"");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private double calculate(Long level1, Long level2, Long level3, Long level4) {
        Long a = level1+level2+level3+level4;
        Log.d("Progress4",a+"");
        return a * 2.5;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        coin = findViewById(R.id.coinview3);
        db  = FirebaseFirestore.getInstance();
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
                try {
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
