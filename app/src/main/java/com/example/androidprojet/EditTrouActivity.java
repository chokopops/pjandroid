package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTrouActivity extends AppCompatActivity {

    private TextView tveditdistance, tveditpar;
    private EditText eteditpar, eteditdistance;
    private ImageView ivedittrou;
    private Button btedittrou;
    private String trouImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trou);

        Intent i = getIntent();
        String golfname = (String)i.getStringExtra("golfname");
        String parcourname = (String)i.getStringExtra("parcourname");
        String trouname = (String)i.getStringExtra("trouname");

        tveditpar = (TextView)findViewById(R.id.tveditpar);
        tveditdistance = (TextView)findViewById(R.id.tveditdistance);

        eteditpar = (EditText)findViewById(R.id.eteditpar);
        eteditdistance = (EditText)findViewById(R.id.eteditdistance);

        ivedittrou = (ImageView)findViewById(R.id.ivedittrou);

        btedittrou = (Button)findViewById(R.id.btedittrou);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Golf").document(golfname).collection("parcours")
                .document(parcourname).collection("trous").document(trouname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            trouImageUrl = document.getString("image");

                            Picasso.get().load(trouImageUrl).into(ivedittrou);

                            eteditpar.setText(document.getString("par"));
                            eteditdistance.setText(document.getString("distance"));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        btedittrou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> edittrou = new HashMap<>();
                edittrou.put("distance", eteditdistance.getText().toString());
                edittrou.put("image", trouImageUrl);
                edittrou.put("par", eteditpar.getText().toString());

                db.collection("Golf").document(golfname).collection("parcours")
                        .document(parcourname).collection("trous").document(trouname)
                        .set(edittrou)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditTrouActivity.this, "Hole successfully edited", Toast.LENGTH_SHORT).show();
                                Log.i("tag", "User créer sur firesbase");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditTrouActivity.this, "Error while trying to edit", Toast.LENGTH_SHORT).show();
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
            }
        });
    }
}