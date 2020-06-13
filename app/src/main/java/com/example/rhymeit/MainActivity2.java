package com.example.rhymeit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity2 extends AppCompatActivity {
    final String URl1 = "https://api.datamuse.com/words?";
    final String URl = "https://api.datamuse.com/words?sp=";
    private static final long START_TIME_IN_MILLIS = 30000;
    FirebaseFirestore db;
    private static long TIMER;
    private CountDownTimer mCountDownTimer;
    String URL3, URL2, url,uid,finaltext,asd;
    private RequestQueue mRequestQueue;
    ProgressBar mProgressBar;
    Animation blink;
    ImageView first, second, third;
    EditText userinput;
    TextView scoreview,coin;
    Animation.AnimationListener mAnimationListener;
    CircularProgressBar timecircle;
    ListView interactionlist;
    List<String> used = new ArrayList<>();
    List<String> messages = new ArrayList<>();
    List<Boolean> direction = new ArrayList<>();
    List<String> keywords;
    Adapter mAdapter;
    int i = 0, j = 0,l;
    FitButton sendbtn,hintbtn;
    DocumentReference note;
    Integer l1,l2,l3,l4;
    public static final String name ="Progress";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mCountDownTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setStatusBarColor(Color.parseColor("#B93963"));

        //Firestone
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        note = db.document("UserProfile/"+uid);
        //Initialize keywords
        keywords = new ArrayList<>();
        keywords.add("buck");
        keywords.add("home");
        keywords.add("school");
        keywords.add("duck");
        keywords.add("holy");

        //attach views
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
        coin = findViewById(R.id.coincount);
        mProgressBar.setMax(10);
        timecircle.setProgressMax(START_TIME_IN_MILLIS);
        scoreview = findViewById(R.id.scoreview2);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        blink.setAnimationListener(mAnimationListener);
        first = findViewById(R.id.firstlive);
        second = findViewById(R.id.secondlive);
        third = findViewById(R.id.thirdlive);





    }

    private String dictionaryURl(String word){
        final String language = "en";
        final String word_id=word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
    }

    @Override
    protected void onStart() {
        super.onStart();
        first.startAnimation(blink);
        second.startAnimation(blink);
        third.startAnimation(blink);
        suitableInput(getLevelno());
        Random random = new Random();
        getinput(random.nextInt(4));

        note.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity2.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d("Exception", e.toString());
                    return;
                }
                if(documentSnapshot.exists()){
                    coin.setText(documentSnapshot.get("Coin").toString());
                    l1 = Integer.parseInt(documentSnapshot.get("level1").toString());
                    l2 = Integer.parseInt(documentSnapshot.get("level2").toString());
                    l3 = Integer.parseInt(documentSnapshot.get("level3").toString());
                    l4 = Integer.parseInt(documentSnapshot.get("level4").toString());
                }
            }
        });

        hintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help(used.get(used.size()-1));
            }
        });
        sendbtn.setEnabled(true);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(userinput.getText().toString().toLowerCase())){
                    if (!checkIfUsed(userinput.getText().toString().toLowerCase())) {
                        if (getNoChar(getLevelno()) <= userinput.getText().toString().trim().length()) {
                            if (checkRhyme(userinput.getText().toString().toLowerCase())) {
                                url = dictionaryURl(userinput.getText().toString().toLowerCase());
                                DictionaryRequest myDictionaryRequest = new DictionaryRequest(MainActivity2.this);
                                Log.d("Async","1Inside");
                                myDictionaryRequest.execute(url);
                                sendbtn.setEnabled(false);
                                hintbtn.setEnabled(false);
                            } else {
                                FancyToast.makeText(MainActivity2.this, "It does not rhyme with other word", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                reduceLives();
                                userinput.setText("");
                            }
                        } else {
                            FancyToast.makeText(MainActivity2.this, "The minimum character criteria is " + getNoChar(getLevelno()), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            reduceLives();
                            userinput.setText("");
                        }
                    } else {
                        FancyToast.makeText(MainActivity2.this, "You have used it", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        reduceLives();
                        userinput.setText("");
                    }
                }else{
                    FancyToast.makeText(MainActivity2.this,"Can't send empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    userinput.setText("");
                }
            }
        });
    }

    private void suitableInput(int levelno) {
        switch (levelno){
            case 2:
                messages.add("Welcome to RhymeIt level 2. Minimum word length is 2");
                direction.add(false);
                used.add("Welcome to RhymeIt level 2. Minimum word length is 2");
                mAdapter.notifyDataSetChanged();
                break;
            case 3:
                messages.add("Welcome to RhymeIt level 3. Minimum word length is 3");
                direction.add(false);
                used.add("Welcome to RhymeIt level 3. Minimum word length is 3");
                mAdapter.notifyDataSetChanged();
                break;
            case 4:
                messages.add("Welcome to RhymeIt level 4. Minimum word length is 4");
                direction.add(false);
                used.add("Welcome to RhymeIt level 4. Minimum word length is 4");
                mAdapter.notifyDataSetChanged();
                break;
            default:
                messages.add("Welcome to RhymeIt level 1.No minimum length");
                direction.add(false);
                used.add("Welcome to RhymeIt level 1.No minimum length");
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void help(String s) {
        Character ch;
        ch = s.charAt(s.length()-1);
        URL3 = URl + ch+"??";
        Log.d("Reply",URL3);
        getSuitableReply(URL3);
    }

    private void getSuitableReply(String url3) {
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url3, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray =response;
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if((!checkIfUsed(jsonObject.getString("word"))) && getNoChar(getLevelno())<=jsonObject.getString("word").length()){
                                    Log.d("Reply",jsonObject.getString("word"));
                                    messages.add(jsonObject.getString("word"));
                                    direction.add(true);
                                    used.add(jsonObject.getString("word"));
                                    mAdapter.notifyDataSetChanged();
                                    i=i+1;
                                    mProgressBar.setProgress(i);
                                    if (mProgressBar.getProgress() == 10) {
                                        mCountDownTimer.cancel();
                                        Map<String, Object> score = new HashMap<>();
                                        score.put("level"+getLevelno(),l);
                                        //                                    db.collection("Score").document("level"+l).set(score);
                                        if (getlevelprogress() < l) {
                                            note.update(score);
                                        }
                                        SharedPreferences sharedPreferences = getSharedPreferences("Progress",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("score",l);
                                        editor.apply();
                                        editor.putInt("level",getLevelno());
                                        editor.commit();
                                        Successful_dialog dialog = new Successful_dialog();
                                        dialog.setCancelable(false);
                                        dialog.show(getSupportFragmentManager(), "Successful");

                                    }
                                    getReply(jsonObject.getString("word"));
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

    private void getinput(int nextInt) {
        userinput.setTextSize(24);
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

    //Adds the message to the list
    private void getMessage(String word) {
        messages.add(word);
        Log.d("word", word+"2");
        direction.add(false);
        used.add(word);
        mAdapter.notifyDataSetChanged();
    }

    //Starts time
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
            FancyToast.makeText(MainActivity2.this,"You have lost first life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else{
            if (second.getVisibility() == View.VISIBLE){
                second.setVisibility(View.INVISIBLE);
                second.clearAnimation();
                FancyToast.makeText(MainActivity2.this,"You have lost second life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }else {
                if(third.getVisibility() == View.VISIBLE){
                    third.setVisibility(View.INVISIBLE);
                    third.clearAnimation();
                    FancyToast.makeText(MainActivity2.this,"You have lost third life",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else{
                    mCountDownTimer.cancel();
                    Map<String, Object> score = new HashMap<>();
                    score.put("level"+getLevelno(),l);
//                                    db.collection("Score").document("level"+l).set(score);
                    if (getlevelprogress() < l) {
                        note.update(score);
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("Progress",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("score",l);
                    editor.apply();
                    editor.putInt("level",getLevelno());
                    editor.commit();
                    FailedDialog dialog = new FailedDialog();
                    dialog.setCancelable(false);
                    dialog.show(getSupportFragmentManager(),"Failed");
                }
            }
        }
    }

    private int getlevelprogress() {
        switch (getLevelno()){
            case 2:
                return l2;
            case 3:
                return l3;
            case 4:
                return l4;
            default:
                return l1;
        }
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
                    if (getlevelprogress() < l) {
                        note.update(score);
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("Progress",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("score",l);
                    editor.apply();
                    editor.putInt("level",getLevelno());
                    editor.commit();
                    FailedDialog dialog = new FailedDialog();
                    dialog.setCancelable(false);
                    dialog.show(getSupportFragmentManager(),"Failed");
                }
            }
        }
    }


    private int getLevelno() {
        return getIntent().getIntExtra("level",1);
    }

    private void sendTheMessage(String toString) {
        messages.add(toString);
        direction.add(true);
        used.add(toString);
        mAdapter.notifyDataSetChanged();
        getReply(toString);
    }

    private void getReply(String use) {
        Character ch;
        ch = use.charAt(use.length()-1);
        URL3 = URl + ch+"??";
        Log.d("Reply",URL3);
        getRhymingWord(URL3);
    }

    private void getRhymingWord(String url3) {
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

    private boolean checkRhyme(String user) {
        int l = used.size()-1;
        String muri = used.get(l).toLowerCase();
        Character ch=  muri.charAt(muri.length()-1);
        if(ch == user.toLowerCase().charAt(0)){
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

    private int getNoChar(int levelno) {
        int no;
        if(levelno == 1){
            no = 0;
        }else if(levelno == 2){
            no = 2;
        }else if(levelno == 3) {
            no = 3;
        }else {
            no =4;
        }
        return no;
    }



    //Check if the word is meaningful
    class DictionaryRequest extends AsyncTask<String,Integer,String>{
        final String app_id = "5ff4f478";
        final String api_key="44df5f35344c630ae4ebcf6b0de4052d";
        String url1,result;
        Context mContext;
        String def,sd;

        public String getDef() {
            return def;
        }

        public DictionaryRequest(Context context) {
            mContext = context;
        }



        @Override
        protected String doInBackground(String... params) {
            url1 = params[0];
            try {
                URL url = new URL(url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Accept","application/json");
                httpURLConnection.setRequestProperty("app_id",app_id);
                httpURLConnection.setRequestProperty("app_key",api_key);

                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                } catch (FileNotFoundException e) {
                    sd = "null";
                    return sd;
                }
                StringBuilder stringBuilder = new StringBuilder();

                String line=null;
                while ((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line+"\n");
                }
                result =  stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();

            }
            if (result != null) {
                try {
                    Log.d("sd",result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    JSONObject Entries =jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = Entries.getJSONArray("lexicalEntries");

                    JSONObject entries = jsonArray1.getJSONObject(0);
                    JSONArray e = entries.getJSONArray("entries");

                    JSONObject jsonObject1 = e.getJSONObject(0);
                    JSONArray sense = jsonObject1.getJSONArray("senses");

                    JSONObject d = sense.getJSONObject(0);
                    JSONArray de = d.getJSONArray("definitions");

                    def = de.getString(0);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                def = sd;
            }
            return def;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            NetworkTask mTask = new NetworkTask(MainActivity2.this,s);
            Log.d("Async","2Inside");

            mTask.execute(userinput.getText().toString().toLowerCase());

    }
    }

    class NetworkTask extends AsyncTask<String, Integer, String> {
        WeakReference<MainActivity2>mWeakReference;
        String url,text,verified,asd;

        public NetworkTask(MainActivity2 activity,String asd) {
            mWeakReference = new WeakReference<MainActivity2>(activity);
            this.asd = asd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("Async","preexecute");
            i=i+1;
            mProgressBar.setProgress(i);

        }

        @Override
        protected String doInBackground(String... strings) {
            text = strings[0];
            Log.d("Async","background");
            if (asd != "null") {
                            Log.d("Async", "Inside");
                            verified = text;

            }
                else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(MainActivity2.this, "This is not a meaningful word", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                reduceLives();
                                verified = null;
                            }
                        });

                    }


            return verified;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Async","postexecute");
            if (s != null) {
                mCountDownTimer.cancel();
                l = l + 1;
                scoreview.setText("Score: " + l + "/" + "10");
                if (mProgressBar.getProgress() == 10) {
                    mCountDownTimer.cancel();
                    Map<String, Object> score = new HashMap<>();
                    score.put("level"+getLevelno(),l);
    //                                    db.collection("Score").document("level"+l).set(score);
                    if (getlevelprogress() < l) {
                        note.update(score);
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("Progress",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("score",l);
                    editor.apply();
                    editor.putInt("level",getLevelno());
                    editor.commit();
                    Successful_dialog dialog = new Successful_dialog();
                    dialog.setCancelable(false);
                    dialog.show(getSupportFragmentManager(), "Successful");

                }
                else {
                    sendTheMessage(s);
                    sendbtn.setEnabled(true);
                    hintbtn.setEnabled(true);
                }
            }
            userinput.setText("");

        }
    }
}
