package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfilActivity extends AppCompatActivity {

    private TextView email, username, golf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        email = (TextView)findViewById(R.id.emailtv2);
        username = (TextView)findViewById(R.id.usernametv2);
        golf = (TextView)findViewById(R.id.golftv2);

        Intent i = getIntent();
        Users user = (Users)i.getSerializableExtra("user");

        email.setText(user.getEmail());
        username.setText(user.getUsername());
        golf.setText(user.getGolf());

    }
}