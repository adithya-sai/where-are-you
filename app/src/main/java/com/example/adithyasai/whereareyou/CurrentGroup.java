package com.example.adithyasai.whereareyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by adithyasai on 4/8/17.
 */

public class CurrentGroup extends Fragment {
    String latitude;
    String longitude;

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
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        json= new String();
        latitude= new String();
        longitude=new String();
        getActivity().setTitle("Current Group");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        if (!latitude.isEmpty()) {
                            System.out.println(getLatitude()+" "+getLongitude()+" "+getJson());
                        } else {
                            System.out.println("empty");
                        }
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getContext().registerReceiver(br,new IntentFilter("com.example.adithyasai.whereareyou.CurrentLocationBackground"));
    }

}
