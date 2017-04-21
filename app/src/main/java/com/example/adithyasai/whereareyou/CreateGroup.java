package com.example.adithyasai.whereareyou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/*********Gautham**********/
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
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
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;
/*********Gautham**********/

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by adithyasai on 4/8/17.
 */

public class CreateGroup extends Fragment {
    /******Gautham*****/
    Context context;
    String googleKey = "AIzaSyCL_FXN7Pr89d3d_4W8O4kHlUa-nJcgXo0";
    private LocationManager locationManager;
    private String provider;
    private TextInputEditText groupName;
    private String authKey;
    private String userKey;
    private String[] coordinates;
    public String getDest_coordinates() {
        return dest_coordinates;
    }

    public void setDest_coordinates(String dest_coordinates) {
        this.dest_coordinates = dest_coordinates;
    }

    String dest_coordinates;

    JSONObject js;
    /******Gautham*****/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();
        getActivity().setTitle("Create new group");
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPrefs",MODE_WORLD_READABLE);
        userKey=sp.getString("userKey","default");
        authKey=sp.getString("authKey","default");
        Toast.makeText(getActivity(),userKey,Toast.LENGTH_SHORT).show();
        setDest_coordinates(getCoordinates());
        coordinates=getDest_coordinates().split(",");
        Button btnCreateGroup = (Button) view.findViewById(R.id.btn_create_group);
        btnCreateGroup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(this,AddMembers.class);
                Bundle bundle=new Bundle();
                bundle.putString("authKey",authKey);
                bundle.putString("authKey",userKey);
                bundle.putString("latitude",coordinates[0]);
                bundle.putString("longitude",coordinates[1]);
                intent.putExtras(bundle);
            }
        });


    }

    public String getCoordinates()
    {
        //Setting locationManager configs
        this.locationManager = (LocationManager) this.getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = LocationManager.GPS_PROVIDER; //Specifying usage of GPS Provider
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("No permission");
            return "";
        }
        String urlLocation;
        Button submit = (Button) this.getActivity().findViewById(R.id.btn_create_group);

        System.out.println("Inside submit button");

        urlLocation = buildUrlDestinationCoordinates();
        AsyncHttpPost a_call = new AsyncHttpPost(context);
        String[] u = new String[2];
        u[0]="googleapi";
        u[1] = urlLocation;
        String cur_lat="";
        String cur_long="";
        try {
            setJSON(a_call.execute(u).get());
            cur_lat=js.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
            cur_long=js.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
        }catch (InterruptedException ie)
        {

        }catch (ExecutionException ee)
        {

        }
        catch (JSONException je)
        {

        }
        return cur_lat+","+cur_long;
    }

    public String buildUrlDestinationCoordinates()
    {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        url += getAddress().replace(" ", "+");
        url += "&key=" + googleKey;
        return url;
    }

    public void setJSON(String json)
    {
        try
        {
            js = new JSONObject(json);
        }
        catch(JSONException je)
        {

        }
    }

    /*************************Obtain Values from TextFields for address*********************/
    //Called in  buildUrl
    protected String getAddress()
    {
        System.out.println("Inside getAddress");
        TextInputEditText addr=(TextInputEditText) this.getActivity().findViewById(R.id.input_street);
        TextInputEditText ci=(TextInputEditText) this.getActivity().findViewById(R.id.input_city);
        TextInputEditText st = (TextInputEditText) this.getActivity().findViewById(R.id.input_state);
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
    /*************************Obtain Values from TextFields for address*********************/


}


