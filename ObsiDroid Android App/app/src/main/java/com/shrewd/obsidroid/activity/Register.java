package com.shrewd.obsidroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shrewd.obsidroid.R;

public class Register extends AppCompatActivity {
    TextInputEditText email, username, password, cpassword;
    Button register;
    TextView loginText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        register = findViewById(R.id.register);
        loginText = findViewById(R.id.loginText);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            /*startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();*/
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString().trim();
                if(TextUtils.isEmpty(em)) {
                    email.setError("Email is Required.");
                    return;
                }

                String user = username.getText().toString().trim();
                if(TextUtils.isEmpty(user)) {
                    username.setError("Username is Required.");
                    return;
                }

                String pass = password.getText().toString().trim();
                if(TextUtils.isEmpty(pass)) {
                    email.setError("Password is Required.");
                    return;
                }
                if(pass.length()<8) {
                    email.setError("Password must be 8 characters or longer.");
                    return;
                }

                String cpass = cpassword.getText().toString().trim();
                if(!cpass.equals(pass)) {
                    email.setError("Passwords do not match.");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Register.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Register.this, "Account creation failed !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}