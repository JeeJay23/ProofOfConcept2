package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {


    EditText editTextUsername, editTextPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        findViewById(R.id.textViewSignUp).setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.editTextUsernameSignUp);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswoordSignUp);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.BtnSignUp).setOnClickListener(this);


    }


    private void registerUser(){
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(username.isEmpty()){
            editTextUsername.setError("Email is required");
            editTextUsername.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            editTextUsername.setError("Please enter a valid email");
            editTextUsername.requestFocus();
            return;
        }


        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("The minimum lenght of a password should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textViewSignUp:

                startActivity(new Intent(this, LoginScreen.class));

                break;

            case R.id.BtnSignUp:
                registerUser();
                break;

        }
    }
}
