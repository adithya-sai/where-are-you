package com.example.adithyasai.whereareyou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by subbu on 4/26/17.
 */

public class ManageInvitation extends Fragment {
    private String userKey;
    private String authKey;
    private String result;

    public ManageInvitation(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_manage_invitation, container, false);
        SharedPreferences sp = this.getActivity().getSharedPreferences("MyPrefs",MODE_WORLD_READABLE);
        userKey=sp.getString("userKey","default");
        authKey=sp.getString("authKey","default");



        AsyncHttpPost getInvites= new AsyncHttpPost(getActivity());
        try{
            result=getInvites.execute("get_invite_list",userKey,authKey).get();
            System.out.println(result);
            ManageInvitation mi = new ManageInvitation();
            mi.setText(view, result);


        }
        catch (Exception e){
            System.out.println("Get invite exception");
        }
//        ManageInvitation mi = new ManageInvitation();
//        mi.setText(view, "{'invited_by':'Subbu','invitation_id':'101u', 'event_name': 'Four Peaks meet'}");
        Button btnAccept = (Button) view.findViewById(R.id.accept);
        Button btnReject= (Button) view.findViewById(R.id.reject);
        btnAccept.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                AsyncHttpPost acceptPost=new AsyncHttpPost(getActivity());
                try{
                    JSONObject j=new JSONObject(result);
                    String invitation_id= j.getString("invitation_id");
                    String result=acceptPost.execute("accept_invite",userKey,authKey,invitation_id).get();
                    System.out.println(result);
                    if(result.equals("False"))
                        Toast.makeText(getContext(),"Already accepted",Toast.LENGTH_SHORT).show();
                    else
                    {
                        Intent intent = new Intent(getContext(),CurrentLocationBackground.class);
                        Bundle extras =new Bundle();

                        JSONObject jo = new JSONObject(result);
                        String latitude=jo.getString("latitude");
                        String longitude=jo.getString("longitude");
                        String groupId=jo.getString("event_id");
                        String dest=latitude+","+longitude;
                        extras.putString("groupId",groupId);
                        extras.putString("destination",dest);
                        extras.putString("user",userKey);
                        extras.putString("authKey",authKey);
                        intent.putExtras(extras);
                        getContext().startService(intent);
                        Intent i=new Intent(getContext(),CurrentGroup.class);
                        getContext().startActivity(i);
                    }
                }
                catch (Exception e){
                    System.out.println("button click accept exception");
                }
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                AsyncHttpPost rejectPost=new AsyncHttpPost(getActivity());
                try{
                    JSONObject j=new JSONObject(result);
                    String invitation_id= j.getString("invitation_id");
                    String result=rejectPost.execute("reject_invite",userKey,authKey,invitation_id).get();
                    System.out.println(result);
                }
                catch (Exception e){
                    System.out.println("button click reject exception");
                }
            }
        });

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

