package com.example.adithyasai.whereareyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
    String s;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_group, container, false);
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
}
