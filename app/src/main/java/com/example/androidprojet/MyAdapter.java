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
            convertView = inflater.inflate(R.layout.lignecartedescore_layout, parent, false);
            ((TextView) convertView.findViewById(R.id.trou)).setText(vector.get(position).getPar());
            /*((EditText) convertView.findViewById(R.id.j1)).setText(vector.get(position).getJ1());
            ((EditText) convertView.findViewById(R.id.j2)).setText(vector.get(position).getJ2());
            ((EditText) convertView.findViewById(R.id.j3)).setText(vector.get(position).getJ3());
            ((EditText) convertView.findViewById(R.id.j4)).setText(vector.get(position).getJ4());*/
        }

        return convertView;
    }

    public void dd(LigneCarteDeScore lcds) {
        this.vector.add(lcds);
    }
}

