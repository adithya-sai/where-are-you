package com.example.adithyasai.whereareyou;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class AddMembers extends AppCompatActivity {
    private String userKey;
    private String authKey;
    private String latitude;
    private String longitude;
    private String groupName;
    private TextInputEditText userId1;
    private TextInputEditText userId2;
    private TextInputEditText userId3;
    private TextInputEditText userId4;
    private TextInputEditText userId5;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        Bundle bundle=getIntent().getExtras();
        userKey=bundle.getString("userKey");
        authKey=bundle.getString("authKey");
        latitude=bundle.getString("latitude");
        longitude=bundle.getString("longitude");
        groupName=bundle.getString("groupName");
        System.out.println(userKey);
        System.out.println(authKey);
        System.out.println(latitude);
        System.out.println(longitude);
        userId1=(TextInputEditText) findViewById(R.id.user_1);
        userId2=(TextInputEditText) findViewById(R.id.user_2);
        userId3=(TextInputEditText) findViewById(R.id.user_3);
        userId4=(TextInputEditText) findViewById(R.id.user_4);
        userId5=(TextInputEditText) findViewById(R.id.user_5);
        context=this;

        final ArrayList<String> userList=new ArrayList<String>();
        Button addMembers = (Button) findViewById(R.id.btn_add);
        addMembers.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                userList.removeAll(userList);
                userList.add(userId1.getText().toString());
                userList.add(userId2.getText().toString());
                userList.add(userId3.getText().toString());
                userList.add(userId4.getText().toString());
                userList.add(userId5.getText().toString());
                AsyncHttpPost ap=new AsyncHttpPost(AddMembers.this);
                try{
                    String result=ap.execute("createGroup",latitude,longitude,userKey,authKey,userList.get(0),userList.get(1),userList.get(2),userList.get(3),userList.get(4),groupName).get();
                    if(result.equals("False"))
                        Toast.makeText(AddMembers.this,"User does not exist",Toast.LENGTH_SHORT).show();
                    else
                    {
                        Intent intent = new Intent(context,CurrentLocationBackground.class);
                        Bundle extras =new Bundle();
                        String groupId=result;
                        latitude=latitude.replace(" ","");
                        longitude=longitude.replace(" ","");
                        String dest=latitude+","+longitude;
                        extras.putString("groupId",groupId);
                        extras.putString("destination",dest);
                        extras.putString("user",userKey);
                        extras.putString("authKey",authKey);
                        intent.putExtras(extras);
                        startService(intent);
                        Toast.makeText(AddMembers.this,"Group created",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(AddMembers.this,CurrentGroup.class);
                        getApplicationContext().startActivity(i);


                    }
                }
                catch (Exception e){
                    System.out.println("Add members:"+e.getMessage());
                }
            }
        });
    }
}
