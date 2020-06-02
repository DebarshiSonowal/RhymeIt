package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
  final   String URl1 = "https://api.datamuse.com/words?";
 final String URl = "https://api.datamuse.com/words?sp=";
    private static final long START_TIME_IN_MILLIS = 30000;
    private static long TIMER ;
    private CountDownTimer mCountDownTimer;
 String URL3,URL2,url;
    private RequestQueue mRequestQueue;
    ImageButton  sendbtn,hintbtn;
    ProgressBar mProgressBar;
    Animation blink;
    ImageView first,second,third;
    EditText userinput;
    AnimationListener mAnimationListener;
    CircularProgressBar timecircle;
    TextView vsview;
    ListView interactionlist;
    List<String> used=new ArrayList<>();
    List<String> messages=new ArrayList<>();
    List<Boolean> direction = new ArrayList<>();
    Adapter mAdapter;
    int i =0,j;

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("help",MODE_PRIVATE);
        j = sharedPreferences.getInt("nohelp",0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        first.startAnimation(blink);
        second.startAnimation(blink);
        third.startAnimation(blink);

    }
    private void sendMessage() {
        URL2 = URl1+"ml=duck&sp=b*";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL2, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray =response;
                            Log.d("word","1");
                            Random randomNumGen = new Random();
                            int number = randomNumGen.nextInt(10);
//                                mWordModel = new WordModel(jsonArray.getJSONObject(i).getString("word"),jsonArray.getJSONObject(i).getInt("score"),jsonArray.getJSONObject(i).getInt("numSyllables"));
                            JSONObject jsonObject = jsonArray.getJSONObject(number);
                            messages.add(jsonObject.getString("word"));
                            Log.d("word",jsonObject.getString("word"));
                            direction.add(false);
                            used.add(jsonObject.getString("word"));
                            mAdapter.notifyDataSetChanged();
                            timecircle.setProgress(0);
                            try {
                                mCountDownTimer.cancel();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            TIMER = START_TIME_IN_MILLIS;
                            timecircle.setProgressMax(START_TIME_IN_MILLIS);
                            startTimer();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        sendbtn = findViewById(R.id.sendbtn);
        userinput = findViewById(R.id.userbox);
        timecircle = findViewById(R.id.circularProgressBar);
        interactionlist = findViewById(R.id.listview);
        mRequestQueue = Volley.newRequestQueue(this);
        sendMessage();
        hintbtn = findViewById(R.id.hintbtn);
        mAdapter = new Adapter(this,messages,direction);
        interactionlist.setAdapter(mAdapter);
        mProgressBar.setMax(10);
        blink = AnimationUtils.loadAnimation(this,R.anim.blink);
        blink.setAnimationListener(mAnimationListener);
        first = findViewById(R.id.firstlive);
        second = findViewById(R.id.secondlive);
        third = findViewById(R.id.thirdlive);
        j=0;
        hintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateHelp();
               Toast.makeText(MainActivity.this,j+"",Toast.LENGTH_SHORT).show();
                if (j<=3) {
                    gethelp();
                } else {
                    FailedDialog dialog = new FailedDialog();
                dialog.show(getSupportFragmentManager(),"Failed");
                }
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(userinput.getText())) {
                    url = dictionaryURl(userinput.getText().toString());
                    MyDictionaryRequest myDictionaryRequest = new MyDictionaryRequest(MainActivity.this);
                    try {
                        first.setVisibility(View.INVISIBLE);
                        first.clearAnimation();
                        String asd = myDictionaryRequest.execute(url).get();
                        checkRhyme(userinput.getText().toString());
                        if ((!check(userinput.getText().toString().toLowerCase())) && (asd != "null") && (checkRhyme(userinput.getText().toString().toLowerCase()))) {
                            Log.d("sfafa","inside");
                           send();
                            getreply();
                            //                Alert Dialog
                        } else {
        //                    userinput.setText("");
                            Log.d("sfafa","outside");
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    userinput.setText("");
                }else{
                    Toast.makeText(MainActivity.this,"Write something",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateHelp() {

    }

    private void send() {
        messages.add(userinput.getText().toString().toLowerCase());
        direction.add(true);
        used.add(userinput.getText().toString().toLowerCase());
        mAdapter.notifyDataSetChanged();
    }

    private void gethelp() {
        j = j+1;
        SharedPreferences preferences = getSharedPreferences("help",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("nohelp",j);
        editor.commit();
        int l = used.size()-1;
        String muri = used.get(l);
        URL3 = URl + muri.substring(muri.length()-1)+"??";
        JsonParse1(URL3);
    }

    private void JsonParse1(String url3) {
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray =response;
                            for (int i=0;i<jsonArray.length();i++) {
//                                mWordModel = new WordModel(jsonArray.getJSONObject(i).getString("word"),jsonArray.getJSONObject(i).getInt("score"),jsonArray.getJSONObject(i).getInt("numSyllables"));
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!check(jsonObject.getString("word"))){
                                    messages.add(jsonObject.getString("word"));
                                    direction.add(true);
                                    used.add(jsonObject.getString("word"));
                                    mAdapter.notifyDataSetChanged();
                                    timecircle.setProgress(0);
                                    try {
                                        mCountDownTimer.cancel();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    TIMER = START_TIME_IN_MILLIS;
                                    timecircle.setProgressMax(START_TIME_IN_MILLIS);
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


    private Boolean checkRhyme(String user) {
        int l = used.size()-1;
        String muri = used.get(l);
        Character ch=  muri.charAt(muri.length()-1);
        if(ch == user.charAt(0)){
            return true;
        }
        return false;
    }

    private void getreply() {
        int l = used.size()-1;
        String muri = used.get(l);
        URL3 = URl + muri.substring(muri.length()-1)+"??";
        JsonParse(URL3);
    }

    private Boolean check(String toString) {
        for(int j=0;j<used.size();j++)
        {
            if(toString.equals(used.get(j))){
                return true;
            }
        }
        return false;
    }

    private void JsonParse(String url) {
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray =response;
                            for (int i=0;i<jsonArray.length();i++) {
//                                mWordModel = new WordModel(jsonArray.getJSONObject(i).getString("word"),jsonArray.getJSONObject(i).getInt("score"),jsonArray.getJSONObject(i).getInt("numSyllables"));
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!check(jsonObject.getString("word"))){
                                    messages.add(jsonObject.getString("word"));
                                    direction.add(false);
                                    used.add(jsonObject.getString("word"));
                                    mAdapter.notifyDataSetChanged();
                                    timecircle.setProgress(0);
                                    try {
                                        mCountDownTimer.cancel();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    TIMER = START_TIME_IN_MILLIS;
                                    timecircle.setProgressMax(START_TIME_IN_MILLIS);
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

    private void startTimer() {
       mCountDownTimer = new CountDownTimer(TIMER,1000) {
           @Override
           public void onTick(long millisUntilFinished) {
                    TIMER = millisUntilFinished;
               timecircle.setBackgroundProgressBarColor(Color.BLACK);
                    timecircle.setProgress(TIMER);
           }

           @Override
           public void onFinish() {
               FancyToast.makeText(MainActivity.this,"You have used it",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
               if (first.getVisibility() == View.VISIBLE) {
                   first.setVisibility(View.INVISIBLE);
                   first.clearAnimation();
                   gethelp();
               }else{
                   if (second.getVisibility() == View.VISIBLE){
                       second.setVisibility(View.INVISIBLE);
                       second.clearAnimation();
                       gethelp();
                   }else {
                       if(third.getVisibility() == View.VISIBLE){
                           third.setVisibility(View.INVISIBLE);
                           third.clearAnimation();
                           gethelp();
                       }else{
                           FailedDialog dialog = new FailedDialog();
                           dialog.show(getSupportFragmentManager(),"Failed");
                       }
                   }
               }
           }
       };
       mCountDownTimer.start();
        i= i+1;
        mProgressBar.setProgress(i);
    }

    private String dictionaryURl(String word){
        final String language = "en";
       final String word_id=word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
    }



}

