package com.example.gautham.googleapitest;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by gautham on 4/8/17.
 */

public class APICall extends AsyncTask<String, String, String> {

    private Context context;
    public APICall(Context context){this.context=context;}

    @Override
    protected String doInBackground(String... url) {
        try
        {
        return getJSON(url[0]);
        }catch(Exception e){
            return e.getMessage();
        }
    }
    protected String getJSON(String urlstring) throws IOException, JSONException
    {
        System.out.println("Inside getJSON APICall");
        InputStream is = new URL(urlstring).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readJSON(rd);
            JSONObject json = new JSONObject(jsonText);
            return jsonText;
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            is.close();
        }
        return null;
    }
    private static String readJSON(Reader rd) throws IOException {
        System.out.println("Inside readJSON APICall");
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
