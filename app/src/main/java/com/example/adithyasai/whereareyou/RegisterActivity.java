package com.example.adithyasai.whereareyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailView;
    private EditText passwordView;
    private EditText repeatPasswordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailView = (EditText) findViewById(R.id.input_email);
        passwordView = (EditText) findViewById(R.id.input_password);
        repeatPasswordView = (EditText) findViewById(R.id.input_repeat_password);
        Button registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
                new AsyncHttpPost(RegisterActivity.this).execute("register", emailView.toString(), passwordView.toString(),repeatPasswordView.toString());
            }
        });
    }

    public void onLoginLink(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
