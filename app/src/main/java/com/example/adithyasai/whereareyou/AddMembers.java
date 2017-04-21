package com.example.adithyasai.whereareyou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddMembers extends AppCompatActivity {
    private String userKey;
    private String authKey;
    private String latitude;
    private String longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        Bundle bundle=getIntent().getExtras();
        userKey=bundle.getString("userKey");
        authKey=bundle.getString("authKey");
        latitude=bundle.getString("latitude");
        longitude=bundle.getString("longitude");
        System.out.println(userKey);
        System.out.println(authKey);
        System.out.println(latitude);
        System.out.println(longitude);
    }
}
