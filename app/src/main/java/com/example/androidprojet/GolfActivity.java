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
import android.widget.TextView;
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
    private List<String> parcoursList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golf);

        Intent i = getIntent();
        String golfname = (String)i.getStringExtra("golfname");

        listview = (ListView)findViewById(R.id.listviewparcours);

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
                            listview.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
            {
                String parcourname = parent.getItemAtPosition(position).toString();

                Toast.makeText(GolfActivity.this, "redirected to "+parcourname+" page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ParcoursActivity.class);
                intent.putExtra("golfname", golfname);
                intent.putExtra("parcourname", parcourname);
                startActivity(intent);
            }
        });
    }
}