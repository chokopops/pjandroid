package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrouActivityCarte extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private TextView tvdistance, tvtrou, tvpar;
    private EditText j1score, j2score, j3score, j4score;
    private ImageView ivtrou;
    private Button enregistrerscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trou_carte);

        Intent i = getIntent();
        Users user = (Users) i.getSerializableExtra("user");
        String idcarte = (String)i.getStringExtra("idcarte");
        String golfname = (String)i.getStringExtra("golfname");
        String parcourname = (String)i.getStringExtra("parcourname");
        String trouname = (String)i.getStringExtra("trouname");


        tvdistance = (TextView)findViewById(R.id.tvdistancecarte);
        tvtrou = (TextView)findViewById(R.id.troucarte);
        tvpar = (TextView)findViewById(R.id.parcarte);

        j1score = (EditText)findViewById(R.id.j1scorecarte);
        j2score = (EditText)findViewById(R.id.j2scorecarte);
        j3score = (EditText)findViewById(R.id.j3scorecarte);
        j4score = (EditText)findViewById(R.id.j4scorecarte);

        ivtrou = (ImageView)findViewById(R.id.ivtroucarte);

        enregistrerscore = (Button)findViewById(R.id.enregistrerscorescarte);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Golf").document(golfname).collection("parcours")
                .document(parcourname).collection("trous").document(trouname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            DocumentSnapshot document = task.getResult();

                            String trouImageUrl = document.getString("image");
                            Picasso.get().load(trouImageUrl).into(ivtrou);

                            tvdistance.setText(document.getString("distance"));

                            tvpar.setText(document.getString("par"));
                            tvtrou.setText(trouname);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("Users").document(user.getEmail()).collection("cartesdescores").document(idcarte).collection("cartedescore")
                .document(trouname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            j1score.setText(document.getString("j1"));
                            j2score.setText(document.getString("j2"));
                            j3score.setText(document.getString("j3"));
                            j4score.setText(document.getString("j4"));
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        enregistrerscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> score = new HashMap<>();
                score.put("par", tvpar.getText().toString());
                score.put("j1", j1score.getText().toString());
                score.put("j2", j2score.getText().toString());
                score.put("j3", j3score.getText().toString());
                score.put("j4", j4score.getText().toString());

                db.collection("Users").document(user.getEmail()).collection("cartesdescores").document(idcarte).collection("cartedescore")
                        .document(trouname)
                        .set(score)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("tag", "carte de score actualisée");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
            }
        });
    }
}