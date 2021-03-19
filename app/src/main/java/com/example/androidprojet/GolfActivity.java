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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GolfActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> golfList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golf);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listview = (ListView)findViewById(R.id.listviewgolf);

        Intent i = getIntent();
        Users user = (Users)i.getSerializableExtra("user");

        db.collection("Golf")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            golfList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                golfList.add(document.getId());
                                Log.i("TAG", document.getId() + " => " + document.getData());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, golfList);
                            adapter.notifyDataSetChanged();
                            listview.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
            {
                String golfname = parent.getItemAtPosition(position).toString();

                Toast.makeText(GolfActivity.this, "redirected to "+golfname+" page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ParcoursActivity.class);
                intent.putExtra("golfname", golfname);
                startActivity(intent);
            }
        });

        //TODO ajouter les par de chaque trou,
        // ajouter les cartes de scores,
        // ajouter les informations d'un golf après l'onglet golf
        // faire la partie golf en interface
        // ajouter les upload de photo dans le register
        // ajouter les fragment soit en utilisant le tab, soit le drawer, soit le master/detail flow
        // rendre l'interface jolie
    }
}