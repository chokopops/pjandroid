package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class InformationsGolfActivity extends AppCompatActivity {

    private TextView adressegolf, nomgolf, villegolf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_golf);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        adressegolf = (TextView)findViewById(R.id.adressegolf2);
        nomgolf = (TextView)findViewById(R.id.nomgolf2);
        villegolf = (TextView)findViewById(R.id.villegolf2);

        Intent i = getIntent();
        String golfname = i.getStringExtra("golfname");

        db.collection("Golf").document(golfname).collection("informations").document("information")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            adressegolf.setText(document.getString("adresse"));
                            nomgolf.setText(document.getString("nom"));
                            villegolf.setText(document.getString("ville"));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}