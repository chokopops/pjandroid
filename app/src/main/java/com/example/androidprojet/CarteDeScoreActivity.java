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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarteDeScoreActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> trousList = new ArrayList<>();
    private HashMap<String, LigneCarteDeScore> cds = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte_de_score);

        Intent i = getIntent();
        String golfname = i.getStringExtra("golfname");
        String parcourname = i.getStringExtra("parcourname");

        MyAdapter myAdapter = new MyAdapter();

        listview = (ListView)findViewById(R.id.listviewscorestrous);
        listview.setAdapter(myAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Golf").document(golfname).collection("parcours")
                .document(parcourname)
                .collection("trous")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, LigneCarteDeScore> tousLesTrous = new HashMap<String, LigneCarteDeScore>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("get data", document.getId() + " => " + document.getData());
                                String par = document.getData().get("par").toString();
                                LigneCarteDeScore ligneCarteDeScore = new LigneCarteDeScore(par,"0","0","0","0");
                                tousLesTrous.put(document.getId(), ligneCarteDeScore);
                                Log.i("numéro du trou : ", document.getId());
                            }
                            CarteDeScore carteDeScore = new CarteDeScore(parcourname, tousLesTrous);
                            AsyncCds jsp = new AsyncCds(carteDeScore, myAdapter);
                            jsp.execute();

                        } else {
                            Log.d("error get data", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

//todo faire une boucle for pour get tout les trous au lieu de faire deux passage asynchrone dans firebase