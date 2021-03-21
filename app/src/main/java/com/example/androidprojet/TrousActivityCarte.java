package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TrousActivityCarte extends AppCompatActivity {

    private ListView listview;
    private List<String> trousList = new ArrayList<>();
    private Button supprimercarte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trous_carte);

        Intent i = getIntent();
        Users user = (Users) i.getSerializableExtra("user");
        String idcarte = (String) i.getStringExtra("idcarte");
        String golfname = (String) i.getStringExtra("golfname");
        String parcourname = (String) i.getStringExtra("parcourname");

        listview = (ListView) findViewById(R.id.listviewparcourscarte);
        supprimercarte = (Button)findViewById(R.id.supprimercarte);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Golf").document(golfname).collection("parcours").document(parcourname).collection("trous")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            trousList.clear();
                            trousList.add("Carte de score total : ");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                trousList.add(document.getId());
                                Log.i("TAG", document.getId() + " => " + document.getData());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, trousList);
                            adapter.notifyDataSetChanged();
                            listview.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
                String trouname = parent.getItemAtPosition(position).toString();

                if (trouname.equals("Carte de score total : ")){
                    Intent intent = new Intent(getApplicationContext(), CarteDeScoreActivity.class);
                    intent.putExtra("idcarte", idcarte);
                    intent.putExtra("user", user);
                    intent.putExtra("golfname", golfname);
                    intent.putExtra("parcourname", parcourname);
                    intent.putExtra("trouname", trouname);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), TrouActivityCarte.class);
                    intent.putExtra("idcarte", idcarte);
                    intent.putExtra("user", user);
                    intent.putExtra("golfname", golfname);
                    intent.putExtra("parcourname", parcourname);
                    intent.putExtra("trouname", trouname);
                    startActivity(intent);
                }
            }
        });

        supprimercarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(user.getEmail()).collection("cartesdescores").document(idcarte)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                Intent cartespage = new Intent(TrousActivityCarte.this, GolfActivityCartes.class);
                                cartespage.putExtra("user", user);
                                startActivity(cartespage);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                            }
                        });
            }
        });
    }
}