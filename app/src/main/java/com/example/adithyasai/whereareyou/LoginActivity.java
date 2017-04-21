package com.example.adithyasai.whereareyou;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailView;
    private TextInputEditText passwordView;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String user_id = "userKey";
    public static final String auth_key = "authKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        emailView = (TextInputEditText) findViewById(R.id.email);
        passwordView = (TextInputEditText) findViewById(R.id.password);
        Button signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                 AsyncHttpPost ah=new AsyncHttpPost(LoginActivity.this);
                try {
                    String result = ah.execute("signin", emailView.getText().toString(), passwordView.getText().toString()).get();
                    System.out.println(result);
                    if(result.equals("False")){
                        Toast.makeText(LoginActivity.this,"Invalid entry",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_WORLD_READABLE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userKey",emailView.getText().toString());
                        editor.putString("authKey",result);
                        editor.apply();
                        Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    public void onRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@");
//    }
//
//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 4;
//    }
}
