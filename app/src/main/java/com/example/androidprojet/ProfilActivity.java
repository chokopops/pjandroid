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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView imageView;
    private String url = "";
    //a Uri object to store file path
    private Uri filePath;

    private TextView email, username, golf;
    private Button buttonResetPW, buttonUploadImage;
    public Users user = new Users("","","","","");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        imageView = (ImageView)findViewById(R.id.imageView2);

        email = (TextView)findViewById(R.id.emailtv2);
        username = (TextView)findViewById(R.id.usernametv2);
        golf = (TextView)findViewById(R.id.golftv2);

        buttonResetPW = (Button)findViewById(R.id.buttonResetPW);
        buttonUploadImage = (Button)findViewById(R.id.buttonUploadImage);
        Intent i = getIntent();
        user = (Users)i.getSerializableExtra("user");

        email.setText(user.getEmail());
        username.setText(user.getUsername());
        golf.setText(user.getGolf());

        if (!user.getPhotodeprofil().equals("")){
            try{
                Picasso.get().load(user.getPhotodeprofil()).into(imageView);
            }catch (Exception e){
                Log.i("error load image", "error");
            }

        }

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        buttonResetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("reset password", "Email sent.");
                                }
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

                            Log.i("e","ee");
                            db.collection("Users").document(email.getText().toString())
                                    .update("photodeprofil", url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully updated!");
                                    user.setPhotodeprofil(url);
                                    Intent intent = new Intent(ProfilActivity.this, MainMenuUserActivity.class);
                                    intent.putExtra("user",user);
                                    startActivity(intent);
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
                imageView.setImageBitmap(bitmap);
                upload();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}