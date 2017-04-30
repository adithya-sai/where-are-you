package com.example.adithyasai.whereareyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by adithyasai on 4/8/17.
 */

public class CurrentGroup extends Fragment implements OnMapReadyCallback{
    String latitude;
    String longitude;   
    private GoogleMap gMap;
    private TableLayout tl;
    private Double dest_latitude;
    private Double dest_longitude;
//    private MarkerOptions fromCurr;
//    private MarkerOptions toDest;
    Marker m1;
    Marker m2;
    private Double curr_latitude;
    private Double curr_longitude;
    private TextView[] tvArray_user;
    private TextView[] tvArray_eta;
    private TableRow[] trArray;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    String json;
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            setLatitude(intent.getStringExtra("latitude"));
            setLongitude(intent.getStringExtra("longitude"));
            setJson(intent.getStringExtra("json"));
            updateView(getJson());
            updateMap(getLatitude(),getLongitude());
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_group, container, false);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();
        fragment.getMapAsync(this);
        //Destination coordinates
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPrefs",MODE_WORLD_READABLE);
        dest_latitude=Double.parseDouble(sp.getString("latitude","default"));
        dest_longitude=Double.parseDouble(sp.getString("longitude","default"));

        //Table creation
        tl = (TableLayout) view.findViewById(R.id.current_group_table);
        TableRow tr=new TableRow(getContext());
        TextView tv0=new TextView(getContext());
        tv0.setLayoutParams(new TableRow.LayoutParams(1));
        tv0.setText("USER ID");
        tr.addView(tv0);
        TextView tv1=new TextView(getContext());
        tv1.setLayoutParams(new TableRow.LayoutParams(2));
        tv1.setText("ETA");
        tr.addView(tv1);
        tl.addView(tr);
        tvArray_user =new TextView[5];
        tvArray_eta=new TextView[5];
        trArray=new TableRow[5];
        for(int i=0;i<5;i++){
            trArray[i]=new TableRow(getContext());
            tvArray_user[i]=new TextView(getContext());
            tvArray_user[i].setLayoutParams(new TableRow.LayoutParams(1));
            tvArray_user[i].setText("");
            trArray[i].addView(tvArray_user[i]);
            tvArray_eta[i]=new TextView(getContext());
            tvArray_eta[i].setLayoutParams(new TableRow.LayoutParams(2));
            tvArray_eta[i].setText("");
            trArray[i].addView(tvArray_eta[i]);
            tl.addView(trArray[i]);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        json= new String();
        latitude= new String();
        longitude=new String();
        getActivity().setTitle("Current Group");
//        String jsonTest="[{\"eta\":\"2\",\"user_id\":\"a\"},{\"eta\":\"3\",\"user_id\":\"b\"}]";
//        updateView(jsonTest);
    }
    
@Override
    public void onResume()
    {
        super.onResume();
        getContext().registerReceiver(br,new IntentFilter("com.example.adithyasai.whereareyou.CurrentLocationBackground"));
    }


    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap=googleMap;
        m1 = gMap.addMarker(new MarkerOptions()
                .position(
                        new LatLng(0.0,0.0))
                .draggable(true).visible(false));
        m2 = gMap.addMarker(new MarkerOptions()
                .position(
                        new LatLng(0.0,0.0))
                .draggable(true).visible(false));
    }

    public void updateView(String jsonString){
        try {
            JSONArray userListArray = new JSONArray(jsonString);
            for(int i=0;i<userListArray.length();i++){
                JSONObject currentUserJson=userListArray.getJSONObject(i);
                //System.out.println(currentUserJson);
//                TableRow row=new TableRow(getContext());
//                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                trArray[i].setLayoutParams(lp);
//                TextView tv_user=new TextView(getContext());
                tvArray_user[i].setText(currentUserJson.getString("user_id"));
//                TextView tv_eta=new TextView(getContext());
                if(!currentUserJson.getString("eta").equals("0"))
                    tvArray_eta[i].setText(currentUserJson.getString("eta"));
                else
                    tvArray_eta[i].setText("Reached");

            }
        }
        catch (Exception e){
            System.out.println("update text view exception");
            e.printStackTrace();
        }
    }

    public void updateMap(String latitude,String longitude){
        curr_latitude=Double.parseDouble(latitude);
        curr_longitude=Double.parseDouble(longitude);
        try{
            GoogleDirection.withServerKey("AIzaSyCL_FXN7Pr89d3d_4W8O4kHlUa-nJcgXo0")
                    .from(new LatLng(curr_latitude, curr_longitude))
                    .to(new LatLng(dest_latitude,dest_longitude))
                    .avoid(AvoidType.FERRIES)
                    .avoid(AvoidType.HIGHWAYS)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            try{
                                if (direction.isOK()) {
                                    m1.setPosition(new LatLng(curr_latitude,curr_longitude));
                                    m2.setPosition(new LatLng(dest_latitude,dest_longitude));
                                    m1.setTitle("You are here!");
                                    m2.setTitle("Destination");
                                    m1.setVisible(true);
                                    m2.setVisible(true);
                                    ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                    gMap.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED));
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(m1.getPosition());
                                    builder.include(m2.getPosition());
                                    LatLngBounds bounds = builder.build();
                                    int padding=0;
                                    CameraUpdate cu=CameraUpdateFactory.newLatLngBounds(bounds,padding);
                                    gMap.moveCamera(cu);
                                    gMap.animateCamera(cu);

                                } else {
                                    System.out.println("No");
                                }
                            }
                            catch (Exception e){
                                System.out.println("onDirectionSuccess exception");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            System.out.println("error");
                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
