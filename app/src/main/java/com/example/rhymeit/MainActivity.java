package com.example.rhymeit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
  final   String URl1 = "https://api.datamuse.com/words?";
 final String URl = "https://api.datamuse.com/words?sp=";
 String URL3,URL2,url;
    private RequestQueue mRequestQueue;
    ImageButton  sendbtn;
    ProgressBar mProgressBar;
    EditText userinput;
    CircularProgressBar timecircle;
    TextView vsview;
    ListView interactionlist;
    List<String> used=new ArrayList<>();
    List<String> messages=new ArrayList<>();
    List<Boolean> direction = new ArrayList<>();
    Adapter mAdapter;
    @Override
    protected void onStart() {
        super.onStart();

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
        vsview = findViewById(R.id.vsview);
        timecircle = findViewById(R.id.circularProgressBar);
        interactionlist = findViewById(R.id.listview);
        mRequestQueue = Volley.newRequestQueue(this);
        sendMessage();
        mAdapter = new Adapter(this,messages,direction);
        interactionlist.setAdapter(mAdapter);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = dictionaryURl(userinput.getText().toString());
                MyDictionaryRequest myDictionaryRequest = new MyDictionaryRequest(MainActivity.this);
                myDictionaryRequest.execute(url);
                if ((myDictionaryRequest.getDef() == "null")&&(!check(userinput.getText().toString()))) {
                            Log.d("sfafa","inside");
                            messages.add(userinput.getText().toString());
                            direction.add(true);
                            used.add(userinput.getText().toString());
                            mAdapter.notifyDataSetChanged();
                            userinput.setText("");
                            getreply();
                            //                Alert Dialog

                } else {
//                    userinput.setText("");
                    Log.d("sfafa","outside");
                }

            }
        });
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
    private String dictionaryURl(String word){
        final String language = "en";
       final String word_id=word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
    }

}
