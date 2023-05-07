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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivty extends AppCompatActivity {
    private EditText  email, password;
    private Button loginBtn;
    private TextView registerBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);

        //binding activity view to class attributes
        email = findViewById(R.id.emailRegister);
        password = findViewById(R.id.passRegister);
        loginBtn = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.registerLink);
        firebaseAuth = FirebaseAuth.getInstance();

        //Navigating to Register activity if user clicks on register TextView
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivty.this, RegisterActivity.class));
            }
        });

        //Implementing Login Button click
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }
    //Implementing Login Business Logic using FireBaseAuth service
    public void loginUser(){
        String myemail = email.getText().toString();
        String passwd = password.getText().toString();
        //Validating Login fields
        if(!myemail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(myemail).matches()){
            if(!passwd.isEmpty()){
               firebaseAuth.signInWithEmailAndPassword(myemail, passwd)
               .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       Toast.makeText(LoginActivty.this, "Logged In Successfully !", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(LoginActivty.this, MainActivity.class));
                       finish();
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(LoginActivty.this, "Login Failed !", Toast.LENGTH_SHORT).show();
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
