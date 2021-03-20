package com.example.androidprojet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

public class AsyncCds extends AsyncTask<String, Void, LigneCarteDeScore> {

    private LigneCarteDeScore lcds;
    private MyAdapter myAdapter;

    public AsyncCds(LigneCarteDeScore lcds, MyAdapter myAdapter){
        this.lcds = lcds;
        this.myAdapter = myAdapter;
    }

//    @Override
//    protected LigneCarteDeScore doInBackground(String... strings) {
//        LigneCarteDeScore lcds = (LigneCarteDeScore) cds.values();
//        return lcds;
//    }


    @Override
    protected LigneCarteDeScore doInBackground(String... strings) {
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
    protected void onPostExecute(LigneCarteDeScore lcds) {
        try {
            myAdapter.dd(lcds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
