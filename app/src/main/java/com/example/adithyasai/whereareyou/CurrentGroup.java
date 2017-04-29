package com.example.adithyasai.whereareyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by adithyasai on 4/8/17.
 */

<<<<<<< HEAD
public class CurrentGroup extends Fragment implements OnMapReadyCallback{

    private GoogleMap gMap;
=======
public class CurrentGroup extends Fragment {
    String s;
>>>>>>> d65b73b28add4b70965a973a358ddf6782bf3d6b
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
        return view;    }

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
                            System.out.println("hello");
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
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap=googleMap;
        setUpMap();
    }

    public void setUpMap(){
        gMap.setMyLocationEnabled(true);
    }


}
