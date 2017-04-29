package com.example.adithyasai.whereareyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by adithyasai on 4/8/17.
 */

//<<<<<<< HEAD
public class CurrentGroup extends Fragment implements OnMapReadyCallback{

    private GoogleMap gMap;
    private TableLayout tl;
    private String dest_latitude;
    private String dest_longitude;
//=======
    String s;
//>>>>>>> d65b73b28add4b70965a973a358ddf6782bf3d6b

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
        dest_latitude=sp.getString("latitude","default");
        dest_longitude=sp.getString("longitude","default");

        //Table creation
        tl = (TableLayout) view.findViewById(R.id.current_group_table);
        TableRow tr=new TableRow(getContext());
        TextView tv0=new TextView(getContext());
        tv0.setText("USER ID");
        tr.addView(tv0);
        TextView tv1=new TextView(getContext());
        tv1.setText("ETA");
        tr.addView(tv1);
        tl.addView(tr);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Group");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    try{
                        Thread.sleep(5000);
                        if(!s.isEmpty())
                        {
                            System.out.println(s);
                        }
                    }catch(Exception e)
                    {

                    }
                }
            }
        });
        t.start();
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            s=intent.getAction();
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
            System.out.println(intent.getAction());
            updateTextView(s);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap=googleMap;
    }

    public void updateTextView(String jsonString){
        try {
            JSONArray userListArray = new JSONArray(jsonString);
            for(int i=0;i<userListArray.length();i++){
                JSONObject currentUserJson=userListArray.getJSONObject(i);
                TableRow row=new TableRow(getContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView tv_user=new TextView(getContext());
                tv_user.setText(currentUserJson.getString("user_id"));
                row.addView(tv_user);
                TextView tv_eta=new TextView(getContext());
                tv_eta.setText(currentUserJson.getString("eta"));
                row.addView(tv_eta);
                tl.addView(row);
            }
        }
        catch (Exception e){
            System.out.println("update text view exception");
        }
    }
}
