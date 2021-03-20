package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainMenuUserActivity extends AppCompatActivity {

    private Button profil, golf, scorecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_user);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        profil = (Button)findViewById(R.id.profil);
        golf = (Button)findViewById(R.id.golf);
        scorecard = (Button)findViewById(R.id.scorecard);

        Intent i = getIntent();
        Users user = (Users)i.getSerializableExtra("user");

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilpage = new Intent(MainMenuUserActivity.this, ProfilActivity.class);
                profilpage.putExtra("user", user);
                startActivity(profilpage);
            }
        });

        golf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage = new Intent(MainMenuUserActivity.this, GolfActivity.class);
                startActivity(loginPage);
            }
        });

        scorecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartespage = new Intent(MainMenuUserActivity.this, GolfActivityCartes.class);
                cartespage.putExtra("user", user);
                startActivity(cartespage);
            }
        });
    }
}