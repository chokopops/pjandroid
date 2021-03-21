package com.example.androidprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private TextView tvRegister;
    private EditText etEmail, etPassword;
    private Button bSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvRegister = (TextView)findViewById(R.id.registertext);
        etEmail = (EditText)findViewById(R.id.registeremail);
        etPassword = (EditText)findViewById(R.id.registerpassword);
        bSubmit = (Button)findViewById(R.id.registerbutton);

        fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            String email = fAuth.getCurrentUser().getEmail();

            db.collection("Users").document(email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                String email = document.getString("email");
                                String golf = document.getString("golf");
                                String role = document.getString("role");
                                String username = document.getString("username");
                                String photodeprofil = document.getString("photodeprofil");

                                Users user = new Users(username, golf, email, role, photodeprofil);

                                if (role.equals("golf")){
                                    Toast.makeText(RegisterActivity.this, "Redirect to main page", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainMenuGolfActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish();
                                }else if (role.equals("user")){
                                    Toast.makeText(RegisterActivity.this, "Redirected to main page", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainMenuUserActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email is required");
                }

                if(TextUtils.isEmpty(password)){
                    etEmail.setError("Password is required");
                }

                String[] splitArray = null;

                splitArray = email.split("@");

                String username = splitArray[0];
                String finemail = splitArray[1];

                Map<String, String> user = new HashMap<>();
                user.put("email", email);
                user.put("golf", "");
                user.put("photodeprofil", "");
                user.put("role", "user");
                user.put("username", username);
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            db.collection("Users").document(email)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("tag", "User créer sur firesbase");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });

                        }else{
                            Toast.makeText(RegisterActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });
    }
}