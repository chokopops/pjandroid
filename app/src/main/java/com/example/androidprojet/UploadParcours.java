package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadParcours extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 234;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;

    private EditText nombreDeTrous, nomParcours;

    private TextView error;
    private ArrayList<Integer> tousLesTrousTableau = new ArrayList<Integer>();

    private ProgressBar progressBar;

    private String url = "";
    private String parcour = "";
    private String nombreTrou = "";
    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tousLesTrousTableau = new ArrayList<Integer>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_parcours);

        Intent i = getIntent();
        Users user = (Users)i.getSerializableExtra("user");

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.choose);
        buttonUpload = (Button) findViewById(R.id.upload);

        error = (TextView) findViewById(R.id.error);

        nombreDeTrous = (EditText) findViewById(R.id.choisirTrou);
        nomParcours = (EditText) findViewById(R.id.nomParcour);


        imageView = (ImageView) findViewById(R.id.imageView);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nomParcours.getText().toString().replaceAll("\\s", "").matches("") && !nombreDeTrous.getText().toString().matches("")){
                    if (Integer.parseInt(nombreDeTrous.getText().toString()) <= 18){
                        if (filePath != null) {
                            // Create the file metadata
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageMetadata metadata = new StorageMetadata.Builder()
                                    .setContentType("image/jpeg")
                                    .build();
                            UploadTask uploadTask = storage.getReference().child("images/"+filePath.getLastPathSegment()).putFile(filePath, metadata);


                            // Listen for state changes, errors, and completion of the upload.
                            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    Log.d("TAG", "Upload is " + progress + "% done");
                                }
                            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("TAG", "Upload is paused");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storage.getReference().child("images/"+filePath.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.i("url",uri.toString());
                                            url = uri.toString();
                                            parcour = nomParcours.getText().toString();
                                            nombreTrou = nombreDeTrous.getText().toString();
                                            error.setText(nombreTrou);
                                            Map<String, String> imageParcours = new HashMap<>();
                                            imageParcours.put("image parcour", url);
                                            int jsp = Integer.parseInt(nombreTrou);
                                            Map<String, String> golf = new HashMap<>();
                                            golf.put("nom golf", "mon golf");

                                            Log.i("e","ee");
                                            db.collection("Golf").document(user.getGolf()).set(golf);
                                            db.collection("Golf").document(user.getGolf()).collection("parcours")
                                                    .document(parcour).set(imageParcours);
                                            for (int i = 0 ; i < jsp ; i++){
                                                String tp = "error";
                                                if (i<10){
                                                    tp = String.valueOf(i+1);
                                                    tp = "0"+tp;
                                                }
                                                else{
                                                    tp = String.valueOf(i+1);
                                                }

                                                Map<String, String> troutrou = new HashMap<>();
                                                troutrou.put("nom trou", tp);
                                                troutrou.put("par", "0");
                                                troutrou.put("distance", "0");
                                                troutrou.put("image", "");
                                                db.collection("Golf").document(user.getGolf()).collection("parcours")
                                                        .document(parcour).collection("trous").document("trou"+tp)
                                                        .set(troutrou);
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                }
                            });

                        }

                        //if there is not any file
                        else {
                            error.setText("Choisissez une photo");
                        }
                    }
                    else{
                        error.setText("18 trous maximum");
                    }

                }
                else{
                    error.setText("Remplissez tous les champs");
                    Log.i("efe","feij");
                }

            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = null;
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}