package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuGolfActivity extends AppCompatActivity {

    private Button createparcours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_golf);

        Intent i = getIntent();
        Users user = (Users)i.getSerializableExtra("user");

        createparcours = (Button)findViewById(R.id.createparcours);

        createparcours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createPage = new Intent(MainMenuGolfActivity.this, UploadParcours.class);
                createPage.putExtra("user", user);
                startActivity(createPage);
            }
        });
    }
}