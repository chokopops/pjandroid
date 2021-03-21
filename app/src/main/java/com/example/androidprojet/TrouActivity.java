package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrouActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private TextView tvdistance;
    private ImageView ivtrou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trou);

        Intent i = getIntent();
        String golfname = (String)i.getStringExtra("golfname");
        String parcourname = (String)i.getStringExtra("parcourname");
        String trouname = (String)i.getStringExtra("trouname");
        tvdistance = (TextView)findViewById(R.id.tvdistance);
        ivtrou = (ImageView)findViewById(R.id.ivtrou);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Golf").document(golfname).collection("parcours")
                .document(parcourname).collection("trous").document(trouname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            DocumentSnapshot document = task.getResult();

                            String trouImageUrl = document.getString("image");
                            if (!trouImageUrl.equals("")){
                                try{
                                    Picasso.get().load(trouImageUrl).into(ivtrou);
                                }catch (Exception e){
                                    Log.i("error load image", "error");
                                }
                            }



                            tvdistance.setText(document.getString("distance"));
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}