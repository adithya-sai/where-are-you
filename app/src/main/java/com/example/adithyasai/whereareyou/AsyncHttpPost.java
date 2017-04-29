package com.example.adithyasai.whereareyou;


import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

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
            else if(params[0].equals("register")){
                json.put("user_id",params[1]);
                json.put("user_password",params[2]);
                json.put("user_password_repeat",params[3]);

                response = makePostRequest("http://54.218.112.218/create_user_account", json);
//                System.out.println(response);
            }
            else if(params[0].equals("googleapi"))
            {
                response=getJSON(params[1]);
            }
            else if(params[0].equals("createGroup")){
                json.put("latitude",params[1]);
                json.put("longitude",params[2]);
                json.put("admin_user_id",params[3]);
                json.put("admin_auth_token",params[4]);
                ArrayList<String> userList=new ArrayList<String>();
                if(!params[5].equals(""))
                    userList.add(params[5]);
                if(!params[6].equals(""))
                    userList.add(params[6]);
                if(!params[7].equals(""))
                    userList.add(params[7]);
                if(!params[8].equals(""))
                    userList.add(params[8]);
                if(!params[9].equals(""))
                    userList.add(params[9]);
                String stringList=userList.toString()
                        .replace(" ","")
                        .replace("[","")
                        .replace("]","")
                        .trim();
                json.put("user_id_list", stringList);
                json.put("group_name",params[10]);
                response=makePostRequest("http://54.218.112.218/create_group",json);
            }
            else if(params[0].equals("Get ETA"))
            {
                response=getJSON(params[1]);
            }
            else if(params[0].equals("UpdateLocation"))
            {
                json.put("latitude",params[1]);
                json.put("longitude",params[2]);
                json.put("eta",params[3]);
                json.put("user_id",params[4]);
                json.put("auth_token",params[5]);
                json.put("group_id",params[6]);
                response=makePostRequest("http://54.218.112.218/update_location",json);
            }
            else if(params[0].equals("get_invite_list"))
            {
                json.put("user_id",params[1]);
                json.put("auth_token",params[2]);
                response=makePostRequest("http://54.218.112.218/get_invite_list",json);
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


    protected String getJSON(String urlstring) throws IOException, JSONException
    {
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
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}