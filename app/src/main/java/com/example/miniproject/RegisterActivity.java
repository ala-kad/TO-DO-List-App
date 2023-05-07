package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText  password, email;
    private Button registerBtn;
    private TextView loginBtn;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Binding actitvity Views to Class attributes
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginButton);
        myAuth = FirebaseAuth.getInstance();

        //Navigating User to Login activity when he click on Login TextView
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivty.class));
            }
        });

        //Register Button Click
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    // Register Business logic using FireBAseAuth service
    private void createUser(){

        //Getting input values from activity
        String myemail = email.getText().toString();
        String passwd = password.getText().toString();

        //Validating inputs
        if(!myemail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(myemail).matches()){
            if(!passwd.isEmpty()){
                myAuth.createUserWithEmailAndPassword(myemail, passwd)

                //All inputs are valid
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "Registred Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivty.class));
                        finish();
                    }
                })
                 .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Registration Failed !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                password.setError("Empty password is not allowed !");
            }
        }
        else if (myemail.isEmpty()){
            email.setError("Empty email is not allowed !");
        }
        else {
            email.setError("Please enter a valid email");
        }
    }
}