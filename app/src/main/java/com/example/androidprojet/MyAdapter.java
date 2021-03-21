package com.example.androidprojet;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.protobuf.StringValue;

import org.w3c.dom.Text;

import java.util.Vector;

public class MyAdapter extends BaseAdapter {

    public Vector<LigneCarteDeScore> vector;

    public MyAdapter(){
        this.vector = new Vector<LigneCarteDeScore>();
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public Object getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.activity_ligne_carte, parent, false);
            ((TextView) convertView.findViewById(R.id.alltroucarte)).setText(vector.get(position).getTrou());
            ((TextView) convertView.findViewById(R.id.allparcarte)).setText(vector.get(position).getPar());
            ((TextView) convertView.findViewById(R.id.allj1scorecarte)).setText(vector.get(position).getJ1());
            ((TextView) convertView.findViewById(R.id.allj2scorecarte)).setText(vector.get(position).getJ2());
            ((TextView) convertView.findViewById(R.id.allj3scorecarte)).setText(vector.get(position).getJ3());
            ((TextView) convertView.findViewById(R.id.allj4scorecarte)).setText(vector.get(position).getJ4());
        }

        return convertView;
    }

    public void dd(LigneCarteDeScore lcds) {
        this.vector.add(lcds);
    }
}

