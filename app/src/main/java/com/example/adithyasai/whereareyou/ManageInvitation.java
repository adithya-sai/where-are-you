package com.example.adithyasai.whereareyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by subbu on 4/26/17.
 */

public class ManageInvitation extends Fragment {

    public ManageInvitation(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_manage_invitation, container, false);

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

