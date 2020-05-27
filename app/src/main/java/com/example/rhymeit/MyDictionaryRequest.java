package com.example.rhymeit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyDictionaryRequest extends AsyncTask<String,Integer,String> {
    final String app_id = "b2479155";
    final String api_key="6599fe86ba81ff05ed66d05ba2189bb5";
    String url1,result;
    Context mContext;
    String def,sd;

    public String getDef() {
        return def;
    }

    public MyDictionaryRequest(Context context) {
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
        return result;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (!s.equals("null")) {
            try {
                Log.d("sd",s);
                JSONObject jsonObject = new JSONObject(s);
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

                Toast.makeText(mContext,def,Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            def = sd;
            Toast.makeText(mContext,def,Toast.LENGTH_SHORT).show();
        }
    }
}
