package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParcoursCreateCarteActivity extends AppCompatActivity {

    private ListView listviewparcours;
    private List<String> parcoursList = new ArrayList<>();
    private Date date = new Date();
    private String trouname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours_create_carte);

        Intent i = getIntent();
        Users user = (Users)i.getSerializableExtra("user");
        String golfname = (String)i.getStringExtra("golfname");

        listviewparcours = (ListView)findViewById(R.id.listviewparcourscreatecarte);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Golf").document(golfname).collection("parcours")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            parcoursList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                parcoursList.add(document.getId());
                                Log.i("TAG", document.getId() + " => " + document.getData());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, parcoursList);
                            adapter.notifyDataSetChanged();
                            listviewparcours.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        listviewparcours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
            {
                String parcourname = parent.getItemAtPosition(position).toString();

                Date date2 = new Date(date.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
                String d = sdf.format(date2);
                String idcarte = d +" "+golfname+" "+parcourname;
                Log.i("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", idcarte);



                Map<String, String> carteidfirestore = new HashMap<>();
                carteidfirestore.put("carte", idcarte);

                db.collection("Users").document(user.getEmail()).collection("cartesdescores").document(idcarte)
                        .set(carteidfirestore)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("TAG", "id carte ajouté dans firestore");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });

                for (int j=1; j<19; j++){
                    trouname = "trou";
                    if (j<10){
                        trouname += "0"+j;
                    } else {
                        trouname += j;
                    }
                    setCarte(trouname, golfname, parcourname, db, user, idcarte);
                }

                Intent intent = new Intent(getApplicationContext(), GolfActivityCartes.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setCarte(String trouname, String golfname, String parcourname, FirebaseFirestore db, Users user, String idcarte){
        db.collection("Golf").document(golfname)
                .collection("parcours").document(parcourname)
                .collection("trous").document(trouname).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           DocumentSnapshot document = task.getResult();

                           Toast.makeText(ParcoursCreateCarteActivity.this, "Score card successfully created", Toast.LENGTH_SHORT).show();

                           String par = document.getString("par");
                           Map<String, String> score = new HashMap<>();
                           score.put("par", par);
                           score.put("j1", "0");
                           score.put("j2", "0");
                           score.put("j3", "0");
                           score.put("j4", "0");
                           db.collection("Users").document(user.getEmail())
                                   .collection("cartesdescores").document(idcarte)
                                   .collection("cartedescore")
                                   .document(trouname)
                                   .set(score);
                       }
                   });
    }
}