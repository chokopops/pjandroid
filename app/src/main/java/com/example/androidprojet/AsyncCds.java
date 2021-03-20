package com.example.androidprojet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AsyncCds extends AsyncTask<String, Void, CarteDeScore> {

    private CarteDeScore lcds;
    private MyAdapter myAdapter;

    public AsyncCds(CarteDeScore lcds, MyAdapter myAdapter){
        this.lcds = lcds;
        this.myAdapter = myAdapter;
    }


    @Override
    protected CarteDeScore doInBackground(String... strings) {
        Log.i("TAG", "message1");
        return lcds;
    }

    @Override
    protected void onPostExecute(CarteDeScore lcds) {
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String key : lcds.troulist.keySet()) {
                arrayList.add(key);
            }
            Collections.sort(arrayList);
            for (String key: arrayList){
                myAdapter.dd(lcds.troulist.get(key));
            }
            myAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
