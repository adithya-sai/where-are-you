package com.example.adithyasai.whereareyou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by adithyasai on 4/8/17.
 */

public class CreateGroup extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create new group");
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPrefs",MODE_WORLD_READABLE);
        String username=sp.getString("userKey","default");
        Toast.makeText(getActivity(),username,Toast.LENGTH_SHORT).show();
    }
}
