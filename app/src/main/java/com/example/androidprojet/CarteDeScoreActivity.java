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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarteDeScoreActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> trousList = new ArrayList<>();
    private HashMap<String, LigneCarteDeScore> cds = new HashMap<>();
    private TextView t1, t2, t3, t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18;
    private EditText j1score01, j1score02, j1score03, j1score04,j1score05,j1score06,j1score07,j1score08,j1score097,j1score10,j1score11,j1score12,j1score13,j1score14,j1score15,j1score16,j1score17,j1score18;
    private EditText j2score01, j2score02, j2score03, j2score04,j2score05,j2score06,j2score07,j2score08,j2score097,j2score10,j2score11,j2score12,j2score13,j2score14,j2score15,j2score16,j2score17,j2score18;
    private EditText j3score01, j3score02, j3score03, j3score04,j3score05,j3score06,j3score07,j3score08,j3score097,j3score10,j3score11,j3score12,j3score13,j3score14,j3score15,j3score16,j3score17,j3score18;
    private EditText j4score01, j4score02, j4score03, j4score04,j4score05,j4score06,j4score07,j4score08,j4score097,j4score10,j4score11,j4score12,j4score13,j4score14,j4score15,j4score16,j4score17,j4score18;
    private Button enregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carte18trous_layout);

        Intent i = getIntent();
        String golfname = i.getStringExtra("golfname");
        String parcourname = i.getStringExtra("parcourname");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        db.collection("Golf").document(golfname).collection("parcours")
//                .document(parcourname)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//
//                            tailleParcours = document.getString("tailleparcours");
//
//                            if (tailleParcours == "9"){
//                                setContentView(R.layout.carte9trou_layout);
//
//
//                            } else if (tailleParcours == "18"){
//                                setContentView(R.layout.carte18trous_layout);
//
//
//                            }
//                        } else {
//
//                        }
//                    }
//                });

//        MyAdapter myAdapter = new MyAdapter();

//        listview = (ListView)findViewById(R.id.listviewscorestrous);
//        listview.setAdapter(myAdapter);


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
//                                String par = document.getData().get("par").toString();
//                                LigneCarteDeScore ligneCarteDeScore = new LigneCarteDeScore(par,"0","0","0","0");
//                                tousLesTrous.put(document.getId(), ligneCarteDeScore);
//                                Log.i("numéro du trou : ", document.getId());

                            }
//                            CarteDeScore carteDeScore = new CarteDeScore(parcourname, tousLesTrous);
//                            AsyncCds jsp = new AsyncCds(carteDeScore, myAdapter);
//                            jsp.execute();

                        } else {
                            Log.d("error get data", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}