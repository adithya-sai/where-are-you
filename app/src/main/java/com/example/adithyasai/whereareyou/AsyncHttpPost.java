package com.example.adithyasai.whereareyou;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class AsyncHttpPost extends AsyncTask<String, Void, String> {
    private Context cont;

    public AsyncHttpPost(Context cont){
        this.cont=cont;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            JSONObject json = new JSONObject();
            if (params[0].equals("signin")) {
                json.put("userid", params[1]);
                json.put("pwd", params[2]);
            }
            if(params[0].equals("register")){
                json.put("userid",params[1]);
                json.put("pwd",params[2]);
                json.put("repeat_pwd",params[3]);
            }

            String response = makePostRequest("", json);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }


    public String makePostRequest(String urlString, JSONObject json) {
        String result;
        InputStream in=null;
        DataOutputStream out=null;
        try {
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            in=new BufferedInputStream(urlConnection.getInputStream());
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.connect();
            out=new DataOutputStream(urlConnection.getOutputStream());
            out.writeBytes(URLEncoder.encode(json.toString(),"UTF-8"));
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        try{
            result= IOUtils.toString(in,"UTF-8");
            return result;
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result){

        Toast.makeText(cont,result,Toast.LENGTH_LONG).show();
    }
}
