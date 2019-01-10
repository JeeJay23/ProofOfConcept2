package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {


    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.textViewSignUp).setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.editTextUsernameSignUp);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswoordSignUp);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.BtnSignUp).setOnClickListener(this);
    }

    //register the user
    private void registerUser(){
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //check if the username is filled in
        if(username.isEmpty()){
            editTextUsername.setError("Email is required");
            editTextUsername.requestFocus();
            return;
        }

        //check if the email is legit
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            editTextUsername.setError("Please enter a valid email");
            editTextUsername.requestFocus();
            return;
        }

        //check if the password is filled in
        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        //check if the password is longer than 6 character
        if(password.length()<6){
            editTextPassword.setError("The minimum length of a password should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        //show the progressBar
        progressBar.setVisibility(View.VISIBLE);

        //when everything is filled in correctly, send the username and password to Firebase
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //hide the progressBar
                progressBar.setVisibility(View.GONE);
                //check if the registration succeeded
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(SignUpScreen.this, CreateLobby.class));
                    Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                }
                else{
                    //check if the email is already registered
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    //when something is clicked this will trigger
    @Override
    public void onClick(View v) {
        //check for the clicked object
        switch(v.getId()){
            //if the clicked object was the textViewSignUp
            case R.id.textViewSignUp:
                finish();
                //go to the LoginScreen
                startActivity(new Intent(this, LoginScreen.class));
                break;

            //if the clicked object was the BtnSignUp
            case R.id.BtnSignUp:
                registerUser();
                break;
        }
    }
}
