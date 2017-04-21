package com.example.adithyasai.whereareyou;


import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class AsyncHttpPost extends AsyncTask<String, String, String> {
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
            String response="";
            if (params[0].equals("signin")) {
                json.put("user_id", params[1]);
                json.put("user_password", params[2]);
                System.out.println(json.toString());
                response = makePostRequest("http://54.218.112.218/login", json);
//                System.out.println(response);
            }
            if(params[0].equals("register")){
                json.put("user_id",params[1]);
                json.put("user_password",params[2]);
                json.put("user_password_repeat",params[3]);
                System.out.println(json.toString());
                response = makePostRequest("http://54.218.112.218/create_user_account", json);
//                System.out.println(response);
            }
            return response;
        } catch (Exception ex) {
            System.out.println("doInBackground Exception: "+ex.getMessage());
            return ex.getMessage();
        }
    }


    public String makePostRequest(String urlString, JSONObject json) {
        String result;
        InputStream in=null;
        DataOutputStream out=null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.connect();
            out=new DataOutputStream(urlConnection.getOutputStream());
            out.writeBytes(json.toString());
            out.flush();
//            in=new BufferedInputStream(urlConnection.getInputStream());
//            out.close();
//            in.close();
            System.out.println(urlConnection.getResponseCode());
            if(urlConnection.getResponseCode()==200) {
                in = new BufferedInputStream(urlConnection.getInputStream());
            }
            else{
                in=new BufferedInputStream(urlConnection.getErrorStream());
            }
            result= IOUtils.toString(in,"UTF-8");
            return result;
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
//        try{
//
//        }
//        catch (IOException e){
//            System.out.println("IO Exception: "+e.getMessage());
//            return e.getMessage();
//        }
    }

    @Override
    protected void onPostExecute(String result){

//        Toast.makeText(cont,result,Toast.LENGTH_LONG).show();
//        return result;
    }
}
