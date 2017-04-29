package com.example.adithyasai.whereareyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.*;

/**
 * Created by subbu on 4/26/17.
 */

public class ManageInvitation extends Fragment {

    public ManageInvitation(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_manage_invitation, container, false);
        ManageInvitation mi = new ManageInvitation();
        mi.setText(view, "{'invited_by':'Subbu','invitation_id':'101u', 'event_name': 'Four Peaks meet'}");
        return view;
    }

    public void setText(View view, String jsontxt) {

        JSONObject dict = null;
        try {
            dict = (JSONObject) new JSONTokener(jsontxt).nextValue();
        } catch (JSONException e) {
            // Recovery
        }


        TextView meet = (TextView) view.findViewById(R.id.meetname);
        try {
            meet.setText(dict.getString("event_name"));
        }catch (JSONException e){
            //dummy
        }
        TextView invited = (TextView) view.findViewById(R.id.invited);

        try{
            invited.setText(dict.getString(("invited_by")));
        }catch (JSONException e){
            //dummy
        }

    }

}

