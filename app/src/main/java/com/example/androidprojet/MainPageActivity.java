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

public class MainPageActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> golfList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listview = (ListView)findViewById(R.id.listviewgolf);

        Intent i = getIntent();
        Users user = (Users)i.getSerializableExtra("user");

        Log.i("tag", user.getEmail());
        Log.i("tag", user.getGolf());
        Log.i("tag", user.getRole());
        Log.i("tag", user.getUsername());

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

                Toast.makeText(MainPageActivity.this, "redirected to "+golfname+" page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), GolfActivity.class);
                intent.putExtra("golfname", golfname);
                startActivity(intent);
            }
        });
    }
}