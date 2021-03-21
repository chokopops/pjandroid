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

public class EditParcoursActivity extends AppCompatActivity {

    private ListView listviewparcours;
    private List<String> parcoursList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parcours);

        Intent i = getIntent();
        Users user = (Users) i.getSerializableExtra("user");
        String golfname = user.getGolf();

        listviewparcours = (ListView)findViewById(R.id.listvieweditparcours);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Golf").document(golfname).collection("parcours")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            parcoursList.clear();
                            parcoursList.add("Informations");
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
                if (position == 0){
                    Toast.makeText(EditParcoursActivity.this, "redirected to "+golfname+" information page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), EditInformationsGolfActivity.class);
                    intent.putExtra("golfname", golfname);
                    startActivity(intent);
                } else {
                    String parcourname = parent.getItemAtPosition(position).toString();

                    Toast.makeText(EditParcoursActivity.this, "redirected to " + parcourname + " page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), EditTrousActivity.class);
                    intent.putExtra("golfname", golfname);
                    intent.putExtra("parcourname", parcourname);
                    startActivity(intent);
                }
            }
        });
    }
}