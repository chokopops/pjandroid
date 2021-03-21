package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditInformationsGolfActivity extends AppCompatActivity {

    private EditText adressegolf, nomgolf, villegolf;
    private Button editinformationsgolf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_informations_golf);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        adressegolf = (EditText) findViewById(R.id.eteditadressegolf);
        nomgolf = (EditText) findViewById(R.id.eteditnomgolf);
        villegolf = (EditText) findViewById(R.id.eteditvillegolf);

        editinformationsgolf = (Button)findViewById(R.id.editinformationsgolf);

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

        editinformationsgolf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> editinfo = new HashMap<>();
                editinfo.put("adresse", adressegolf.getText().toString());
                editinfo.put("nom", nomgolf.getText().toString());
                editinfo.put("ville", villegolf.getText().toString());

                db.collection("Golf").document(golfname).collection("informations").document("information")
                        .set(editinfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditInformationsGolfActivity.this, "Information successfully edited", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditInformationsGolfActivity.this, "Error while trying to edit", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}