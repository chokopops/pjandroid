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

public class GolfActivityCartes extends AppCompatActivity {

    private ListView listview;
    private List<String> carteslist = new ArrayList<>();
    private String golfname, parcourname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golf_cartes);

        Intent i = getIntent();
        Users user = (Users) i.getSerializableExtra("user");


        listview = (ListView) findViewById(R.id.listviewcartedescores);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(user.getEmail()).collection("cartesdescores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            carteslist.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                carteslist.add(document.getId());

                                String[] splitArray = null;

                                splitArray = document.getId().split(" ");

                                golfname = splitArray[1];
                                parcourname = splitArray[2];

                                Log.i("TAG", document.getId() + " => " + document.getData());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, carteslist);
                            adapter.notifyDataSetChanged();
                            listview.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
                Toast.makeText(GolfActivityCartes.this, "redirected to carte page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CarteDeScoreActivity.class);
                intent.putExtra("golfname", golfname);
                intent.putExtra("parcourname", parcourname);
                startActivity(intent);
            }
        });
    }
}