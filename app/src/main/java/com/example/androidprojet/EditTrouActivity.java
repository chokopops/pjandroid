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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTrouActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String url = "";
    private Uri filePath;

    private TextView tveditdistance, tveditpar;
    private EditText eteditpar, eteditdistance;
    private ImageView ivedittrou;
    private Button btedittrou, buttonUploadImage;
    private String trouImageUrl;

    private String golfname = "", parcourname = "", trouname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trou);

        Intent i = getIntent();
        golfname = (String)i.getStringExtra("golfname");
        parcourname = (String)i.getStringExtra("parcourname");
        trouname = (String)i.getStringExtra("trouname");

        tveditpar = (TextView)findViewById(R.id.tveditpar);
        tveditdistance = (TextView)findViewById(R.id.tveditdistance);

        eteditpar = (EditText)findViewById(R.id.eteditpar);
        eteditdistance = (EditText)findViewById(R.id.eteditdistance);

        ivedittrou = (ImageView)findViewById(R.id.ivedittrou);

        btedittrou = (Button)findViewById(R.id.btedittrou);
        buttonUploadImage = (Button)findViewById(R.id.btuploadimage);

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
                            if (!trouImageUrl.equals("")){
                                try{
                                    Picasso.get().load(trouImageUrl).into(ivedittrou);
                                }catch (Exception e){
                                    Log.i("error load image", "error");
                                }
                            }


                            eteditpar.setText(document.getString("par"));
                            eteditdistance.setText(document.getString("distance"));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btedittrou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("Golf").document(golfname).collection("parcours")
                        .document(parcourname).collection("trous").document(trouname)
                        .update("distance", eteditdistance.getText().toString(),"par", eteditpar.getText().toString())
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

    protected void upload(){
        if (filePath != null) {
            // Create the file metadata
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();
            Date date = new Date();

            UploadTask uploadTask = storage.getReference().child("images/"+ date.getTime() +"_"+ filePath.getLastPathSegment()).putFile(filePath, metadata);


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

                            Log.i("e","ee");
                            db.collection("Golf").document(golfname)
                                    .collection("parcours").document(parcourname)
                                    .collection("trous").document(trouname)
                                    .update("image", url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully updated!");

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("error", "Error updating document", e);
                                        }
                                    });

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = null;
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivedittrou.setImageBitmap(bitmap);
                upload();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}