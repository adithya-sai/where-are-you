package com.example.gautham.googleapitest;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
//import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class FindLocation extends AppCompatActivity implements LocationListener{

    Context context;
    String address;
    String key="AIzaSyCL_FXN7Pr89d3d_4W8O4kHlUa-nJcgXo0";
    Address add;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);
        context=this;
        System.out.println("On create");;
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener((new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                try {
                    System.out.println("Inside submit button");
                    int responseCode;
                    String response="";
                    String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
                    url += getAddress().replace(" ", "+");
                    url += "&key=" + key;
                    System.out.println(url);
                    APICall a_call = new APICall(context);
                    String[] u = new String[1];
                    u[0]=url;
                    a_call.execute(url);

                    System.out.println(lat+" "+lon);
                    Toast.makeText(context,lat+" "+lon,Toast.LENGTH_LONG).show();
                    }catch(Exception e)
                {
                e.printStackTrace();
                }
                }
        }));
        }
    protected String getAddress()
        {
        System.out.println("Inside getAddress");
        EditText addr=(EditText) findViewById(R.id.address1);
        EditText ci=(EditText) findViewById(R.id.city);
        EditText st = (EditText) findViewById(R.id.state);
        String address=addr.getText().toString();
        String city=ci.getText().toString();
        String state =st.getText().toString();
        if(address.isEmpty() && city.isEmpty() && state.isEmpty())
        {
            return "";
        }
        if(address.isEmpty() || address==null)
        {
            if(city.isEmpty() || city==null)
                return state;
            if(state.isEmpty() || state==null)
                return city;
            return city+","+state;


        }
        else if(city.isEmpty() || city==null)
        {
            return address+","+state;
        }
        else if(state.isEmpty() || state==null)
        {
            return address+","+city;
        }
        return address+","+city+","+state;
        }
    private static String readJSON(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    protected JSONObject getJSON(String urlstring) throws IOException, JSONException
        {
            System.out.println("Inside getJSON");
            InputStream is = new URL(urlstring).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readJSON(rd);
                System.out.println(jsonText);
                JSONObject json = new JSONObject(jsonText);
                return json;
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            finally {
                is.close();
            }
            return null;
        }









    /***************************************************Location Listener************************************************************/
    String lat,lon;
    @Override
    public void onLocationChanged(Location loc) {
        System.out.println("Inside onLocationChanged");
        Toast.makeText(context, "Location changed: Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();
        lon=longitude;
        lat=latitude;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    /****************************************************Location Listener************************************************************/
}
