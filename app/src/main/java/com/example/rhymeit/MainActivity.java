package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.nikartm.button.FitButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    final String URl1 = "https://api.datamuse.com/words?";
    final String URl = "https://api.datamuse.com/words?sp=";
    private static final long START_TIME_IN_MILLIS = 30000;
    FirebaseFirestore db;
    //Atanas Nikolaev
    private static long TIMER;
    private CountDownTimer mCountDownTimer;
    String URL3, URL2, url,uid;
    private RequestQueue mRequestQueue;
    ProgressBar mProgressBar;
    Animation blink;
    ImageView first, second, third;
    EditText userinput;
    TextView scoreview;
    Animation.AnimationListener mAnimationListener;
    CircularProgressBar timecircle;
    ListView interactionlist;
    List<String> used = new ArrayList<>();
    List<String> messages = new ArrayList<>();
    List<Boolean> direction = new ArrayList<>();
    Adapter mAdapter;
    int i = 0, j = 0,l;
    List<String> keywords;
    FitButton sendbtn,hintbtn;
    DocumentReference note;
    public static final String name ="Progress";
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
        mCountDownTimer = null;
        mProgressBar =null;
        blink = null;
        mAnimationListener = null;
        timecircle =null;
        System.gc();
    }

    @Override
    protected void onStart() {
        super.onStart();
        first.startAnimation(blink);
        second.startAnimation(blink);
        third.startAnimation(blink);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ryming_game);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Initialize keywords
        keywords = new ArrayList<>();
        keywords.add("buck");
        keywords.add("home");
        keywords.add("school");
        keywords.add("duck");
        keywords.add("holy");
        db = FirebaseFirestore.getInstance();
        note = db.document("UserProfile/"+uid);
        //Attach views
        mProgressBar = findViewById(R.id.progressBar);
        sendbtn = findViewById(R.id.fbtn);
        userinput = findViewById(R.id.userbox2);
        timecircle = findViewById(R.id.circularProgressBar);
        interactionlist = findViewById(R.id.listview);
        mRequestQueue = Volley.newRequestQueue(this);
        hintbtn = findViewById(R.id.fbtn2);
        mAdapter = new Adapter(this, messages, direction);
        interactionlist.setAdapter(mAdapter);
        mProgressBar.setMax(10);
        timecircle.setProgressMax(START_TIME_IN_MILLIS);
        scoreview = findViewById(R.id.scoreview2);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        blink.setAnimationListener(mAnimationListener);
        first = findViewById(R.id.firstlive);
        second = findViewById(R.id.secondlive);
        third = findViewById(R.id.thirdlive);
        l=0;
        //On click listener
        hintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gethelp();
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(!TextUtils.isEmpty(userinput.getText())){
                    url = dictionaryURl(userinput.getText().toString());
                   networking net = new networking(url);
                    net.run();
//                    MyDictionaryRequest myDictionaryRequest = new MyDictionaryRequest(RhymingGame.this);

//                    try {
//                        String asd = myDictionaryRequest.execute(url).get();
//                        i= i+1;
//                        mProgressBar.setProgress(i);
//                       if(!checkIfUsed(userinput.getText().toString().toLowerCase())){
//                            if(asd != "null"){
//                                if (getNoChar(getLevelno()) <= userinput.getText().length()) {
//                                    if(checkRhyme(userinput.getText().toString().toLowerCase())){
//                                        mCountDownTimer.cancel();
//                                        l=l+1;
//                                        scoreview.setText("Score: "+l+"/"+"10");
//                                        SharedPreferences preferences = getSharedPreferences(name,MODE_PRIVATE);
//                                        SharedPreferences.Editor editor = preferences.edit();
//                                        editor.clear();
//                                        editor.putInt("score",l);
//                                        editor.apply();
//                                        if(mProgressBar.getProgress() == 10){
//                                            mCountDownTimer.cancel();
//                                            Successful_dialog dialog = new Successful_dialog();
//                                            dialog.show(getSupportFragmentManager(),"Successful");
//
//                                        }else{
//                                            sendTheMessage(userinput.getText().toString());
//                                            getReply();
//                                        }
//
//
//                                    }else {
//                                        FancyToast.makeText(RhymingGame.this,"It does not rhyme with other word",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
//                                        reduceLives();
//                                    }
//                                }else {
//                                    FancyToast.makeText(RhymingGame.this,"The minimum character criteria is "+getNoChar(getLevelno()),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
//                                }
//                            }else {
//                                FancyToast.makeText(RhymingGame.this,"This is not a meaningful word",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
//                                reduceLives();
//                            }
//                       }else {
//                           FancyToast.makeText(RhymingGame.this,"You have used it",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
//                          reduceLives();
//                       }
//                       userinput.setText("");
//
//                    } catch (ExecutionException | InterruptedException e) {
//                        e.printStackTrace();
//                    }

                }else {
                    Toast.makeText(MainActivity.this,"Write something",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Get the first input from Computer
        Random random = new Random();

        getinput(random.nextInt(4));

    }

    private int getNoChar(int levelno) {
        int no;
        if(levelno == 1){
            no = 0;
        }else if(levelno == 2){
            no = 3;
        }else if(levelno == 3) {
            no = 4;
        }else {
            no =5;
        }
        return no;
    }
    class networking implements Runnable {
        String url2;

        public networking(String url2) {
            this.url2 = url2;
        }

        @Override
        public void run() {

            MyDictionaryRequest myDictionaryRequest = new MyDictionaryRequest(MainActivity.this);
            try {
                String asd = myDictionaryRequest.execute(url2).get();
                i = i + 1;
                mProgressBar.setProgress(i);
                if (!checkIfUsed(userinput.getText().toString().toLowerCase())) {
                    if (asd != "null") {
                        if (getNoChar(getLevelno()) <= userinput.getText().length()) {
                            if (checkRhyme(userinput.getText().toString().toLowerCase())) {
                                mCountDownTimer.cancel();
                                l = l + 1;
                                scoreview.setText("Score: " + l + "/" + "10");
                                SharedPreferences preferences = getSharedPreferences(name, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.putInt("score", l);
                                editor.apply();
                                if (mProgressBar.getProgress() == 10) {
                                    mCountDownTimer.cancel();
                                    Map<String, Object> score = new HashMap<>();
                                    score.put("level"+getLevelno(),l);
//                                    db.collection("Score").document("level"+l).set(score);
                                    note.set(score);
                                    Successful_dialog dialog = new Successful_dialog();
                                    dialog.show(getSupportFragmentManager(), "Successful");

                                } else {
                                    sendTheMessage(userinput.getText().toString());
                                    getReply();
                                }


                            } else {
                                FancyToast.makeText(MainActivity.this, "It does not rhyme with other word", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                reduceLives();
                            }
                        } else {
                            FancyToast.makeText(MainActivity.this, "The minimum character criteria is " + getNoChar(getLevelno()), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    } else {
                        FancyToast.makeText(MainActivity.this, "This is not a meaningful word", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        reduceLives();
                    }
                } else {
                    FancyToast.makeText(MainActivity.this, "You have used it", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    reduceLives();
                }
                userinput.setText("");

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getLevelno() {
        return getIntent().getIntExtra("level",1);
    }

    private void gethelp() {
        int l = used.size()-1;
        String muri =used.get(l);
        URL3 = URl + muri.substring(muri.length()-1)+"??";
        Log.d("Reply",URL3);
        getRhymingWordHelp(URL3);

    }

    private void reduceLives() {
        if (first.getVisibility() == View.VISIBLE) {
            first.setVisibility(View.INVISIBLE);
            first.clearAnimation();
//            FancyToast.makeText(RhymingGame.this,"You have lost first life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else{
            if (second.getVisibility() == View.VISIBLE){
                second.setVisibility(View.INVISIBLE);
                second.clearAnimation();
//                FancyToast.makeText(RhymingGame.this,"You have lost second life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }else {
                if(third.getVisibility() == View.VISIBLE){
                    third.setVisibility(View.INVISIBLE);
                    third.clearAnimation();
//
                }else{
                    mCountDownTimer.cancel();
                    Map<String, Object> score = new HashMap<>();
                    score.put("level"+getLevelno(),l);
//                                    db.collection("Score").document("level"+l).set(score);
                    note.update(score);
                    FailedDialog dialog = new FailedDialog();
                    dialog.show(getSupportFragmentManager(),"Failed");
                }
            }
        }
    }

    private void getReply() {
        int l = used.size()-1;
        String muri =used.get(l);
        URL3 = URl + muri.substring(muri.length()-1)+"??";
        Log.d("Reply",URL3);
        getRhymingWord(URL3);
    }

    private void getRhymingWordHelp(String url3) {
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url3, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray =response;
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!checkIfUsed(jsonObject.getString("word"))){
                                    Log.d("Reply",jsonObject.getString("word"));
                                    messages.add(jsonObject.getString("word"));
                                    direction.add(true);
                                    used.add(jsonObject.getString("word"));
                                    mAdapter.notifyDataSetChanged();
                                    i= i+1;
                                    mProgressBar.setProgress(i);
                                    l=l+1;
                                    scoreview.setText("Score: "+l+"/"+"10");
                                    SharedPreferences preferences = getSharedPreferences(name,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.putInt("score",l);
                                    editor.apply();
                                    if(mProgressBar.getProgress() == 10){
                                        mCountDownTimer.cancel();
                                        Map<String, Object> score = new HashMap<>();
                                        score.put("level"+getLevelno(),l);
//                                    db.collection("Score").document("level"+l).set(score);
                                        note.update(score);
                                        Successful_dialog dialog = new Successful_dialog();
                                        dialog.show(getSupportFragmentManager(),"Successful");

                                    }
//                                    startTimer();
                                    getReply();
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);

    }

    private void getRhymingWord(String url3) {
       rhyme rhyme = new rhyme(url3);
        rhyme.run();
//        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url3, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            JSONArray jsonArray =response;
//                            for (int i=0;i<jsonArray.length();i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                if(!checkIfUsed(jsonObject.getString("word"))){
//                                    Log.d("Reply",jsonObject.getString("word"));
//                                    messages.add(jsonObject.getString("word"));
//                                    direction.add(false);
//                                    used.add(jsonObject.getString("word"));
//                                    mAdapter.notifyDataSetChanged();
//                                    startTimer();
//                                    break;
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        mRequestQueue.add(request);
    }

    private void sendTheMessage(String toString) {
        messages.add(toString);
        direction.add(true);
        used.add(toString);
        mAdapter.notifyDataSetChanged();
    }

    private boolean checkRhyme(String user) {
        int l = used.size()-1;
        String muri = used.get(l);
        Character ch=  muri.charAt(muri.length()-1);
        if(ch == user.charAt(0)){
            return true;
        }
        return false;
    }

    private boolean checkIfUsed(String toString) {
        for(int j=0;j<used.size();j++)
        {
            if(toString.equals(used.get(j))){
                return true;
            }
        }
        return false;
    }

    private void getinput(int nextInt) {
        String string = keywords.get(nextInt);
        URL2 = URl1 + "ml=" + string.toLowerCase() + "&sp=b*";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL2, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray = response;
                            Log.d("word", "1");
                            Random randomNumGen = new Random();
                            int number = randomNumGen.nextInt(10);
                            JSONObject jsonObject = jsonArray.getJSONObject(number);
                            Log.d("word",jsonObject.getString("word")+"1");
                            startTimer();
                            getMessage(jsonObject.getString("word"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);

    }

    private void getMessage(String word) {
        messages.add(word);
        Log.d("word", word+"2");
        direction.add(false);
        used.add(word);
        mAdapter.notifyDataSetChanged();
    }

    private void startTimer() {
        timecircle.setProgress(0);
        try {
            mCountDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCountDownTimer  = new CountDownTimer(START_TIME_IN_MILLIS,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TIMER = millisUntilFinished;
                timecircle.setBackgroundProgressBarColor(Color.BLACK);
                timecircle.setProgress(TIMER);

            }

            @Override
            public void onFinish() {
                reduceLives1();
            }

        };
        mCountDownTimer.start();

    }

    private void reduceLives1() {
        if (first.getVisibility() == View.VISIBLE) {
            first.setVisibility(View.INVISIBLE);
            first.clearAnimation();
            FancyToast.makeText(MainActivity.this,"You have lost first life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else{
            if (second.getVisibility() == View.VISIBLE){
                second.setVisibility(View.INVISIBLE);
                second.clearAnimation();
                FancyToast.makeText(MainActivity.this,"You have lost second life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }else {
                if(third.getVisibility() == View.VISIBLE){
                    third.setVisibility(View.INVISIBLE);
                    third.clearAnimation();
                    FancyToast.makeText(MainActivity.this,"You have lost third life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else{
                    mCountDownTimer.cancel();
                    Map<String, Object> score = new HashMap<>();
                    score.put("level"+getLevelno(),l);
//                                    db.collection("Score").document("level"+l).set(score);
                    note.update(score);
                    FailedDialog dialog = new FailedDialog();
                    dialog.show(getSupportFragmentManager(),"Failed");
                }
            }
        }
    }

    private String dictionaryURl(String word){
        final String language = "en";
        final String word_id=word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
    }
    class rhyme implements Runnable{
        String url3;

        public rhyme(String url3) {
            this.url3 = url3;
        }

        @Override
        public void run() {
            final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url3, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray =response;
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(!checkIfUsed(jsonObject.getString("word"))){
                                        Log.d("Reply",jsonObject.getString("word"));
                                        messages.add(jsonObject.getString("word"));
                                        direction.add(false);
                                        used.add(jsonObject.getString("word"));
                                        mAdapter.notifyDataSetChanged();
                                        startTimer();
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mRequestQueue.add(request);
        }
    }
}

