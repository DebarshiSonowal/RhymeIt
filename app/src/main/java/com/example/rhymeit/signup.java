package com.example.rhymeit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class signup extends AppCompatActivity {
    EditText  txtUserName,txtEmail, txtPassword, txtConfirmPassword;
    FitButton btn_register,btn_login;
    CheckBox agree;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtUserName =  findViewById(R.id.txt_username);
        txtEmail =  findViewById(R.id.txt_email);
        txtPassword =  findViewById(R.id.txt_password);
        txtConfirmPassword =  findViewById(R.id.txt_confirm_password);
        btn_register = findViewById(R.id.signinprogbtn);
        btn_login = findViewById(R.id.loginprogbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        agree = findViewById(R.id.agreebtn);
        db = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this,login.class));

//                btn_login.doneLoadingAnimation();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = txtUserName.getText().toString().trim();
                final String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(signup.this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signup.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signup.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(signup.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(signup.this, "Password too short", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!agree.isChecked()){
                    Toast.makeText(signup.this, "You have not agreed to our Terms & Conditions", Toast.LENGTH_SHORT).show();
                    return;
                }




                if (password.equals(confirmPassword)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    if (task.isSuccessful()) {
                                        user information = new user(
                                                userName,
                                                email
                                        );

                                        Map<String, Object> note = new HashMap<>();
                                        note.put("Email",email);
                                        note.put("Username",userName);
                                        note.put("level1",0);
                                        note.put("level2",0);
                                        note.put("level3",0);
                                        note.put("level4",0);
                                        note.put("level5",0);
                                        note.put("Coin",20);

                                        db.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(signup.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), login.class));
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(signup.this,"Successful",Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(signup.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                                        FirebaseDatabase.getInstance().getReference("user")
//                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                Toast.makeText(signup.this, "Registration Complete", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(getApplicationContext(), login.class));
//                                            }
//                                        });

                                    } else {
                                        Toast.makeText(signup.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(signup.this, "Password did not match", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
