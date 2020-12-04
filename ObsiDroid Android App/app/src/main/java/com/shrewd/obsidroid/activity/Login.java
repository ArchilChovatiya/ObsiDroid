package com.shrewd.obsidroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shrewd.obsidroid.R;

public class Login extends AppCompatActivity {
    TextInputEditText username, password;
    Button login;
    TextView regText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        regText = findViewById(R.id.registerText);
        login = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            /*startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();*/
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = username.getText().toString().trim();
                if (TextUtils.isEmpty(user)) {
                    username.setError("Email is Required.");
                    return;
                }
                String pass = password.getText().toString().trim();
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is Required.");
                    return;
                }
                mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logging in.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("user", user));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Log in failed !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        regText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
    }
}