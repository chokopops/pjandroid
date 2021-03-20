package com.example.androidprojet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

public class AsyncCds extends AsyncTask<String, Void, CarteDeScore> {

    private CarteDeScore lcds;
    private MyAdapter myAdapter;

    public AsyncCds(CarteDeScore lcds, MyAdapter myAdapter){
        this.lcds = lcds;
        this.myAdapter = myAdapter;
    }

//    @Override
//    protected LigneCarteDeScore doInBackground(String... strings) {
//        LigneCarteDeScore lcds = (LigneCarteDeScore) cds.values();
//        return lcds;
//    }


    @Override
    protected CarteDeScore doInBackground(String... strings) {
        Log.i("TAG", "message1");
        return lcds;
    }

//    @Override
//    protected void onPostExecute(LigneCarteDeScore ligneCarteDeScore) {
//        try {
//            for (CarteDeScore carteDeScore : ligneCarteDeScore) {
//                myAdapter.dd(lcds);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onPostExecute(CarteDeScore lcds) {
        try {
            for (LigneCarteDeScore ligneCarteDeScore: lcds.troulist.values()){
                myAdapter.dd(ligneCarteDeScore);
            }
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
