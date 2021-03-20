package com.example.androidprojet;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CarteDeScore {

    private String parcoursname;
    private HashMap<String, LigneCarteDeScore> troulist = new HashMap<String, LigneCarteDeScore>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CarteDeScore(String parcoursname, HashMap<String, LigneCarteDeScore> troulist){
        this.parcoursname = parcoursname;
        this.troulist = troulist;
    }

    public String getParcoursname() {
        return parcoursname;
    }

    public void setParcoursname(String parcoursname) {
        this.parcoursname = parcoursname;
    }

    public HashMap<String, LigneCarteDeScore> getTrouList(){
        return troulist;
    }
}
