package com.example.adithyasai.whereareyou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by subbu on 4/26/17.
 */

public class ManageInvitation extends Fragment {
    private String userKey;
    private String authKey;;

    public ManageInvitation(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_manage_invitation, container, false);

        AsyncHttpPost ah=new AsyncHttpPost(getActivity());
        try{
            SharedPreferences sp = this.getActivity().getSharedPreferences("MyPrefs",MODE_WORLD_READABLE);
            userKey=sp.getString("userKey","default");
            authKey=sp.getString("authKey","default");

            String result = ah.execute("get_invite_list",userKey,authKey).get();
            System.out.println(result);
        }
        catch (Exception e){
            System.out.println("Get invite exception");
        }


        String[] menuItems = {"Invitation 1", "Invitation 2"};

        ListView listview = (ListView) view.findViewById(R.id.managemenu);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                menuItems
        );
        listview.setAdapter(listViewAdapter);
        return view;
    }
}

