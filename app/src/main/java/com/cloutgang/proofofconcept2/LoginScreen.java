package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.textViewLogin).setOnClickListener(this);
        findViewById(R.id.BtnLogin).setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.editTextUsernameLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswoordLogin);


    }

    //when a user tries to login
    private void UserLogin(){
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


        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginScreen.this, ProfileScreen.class);

                    //make sure the user can't go back to the login with the back button
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, ProfileScreen.class));
        }
    }

    //when something is clicked this will trigger
    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, SignUpScreen.class));
                break;

            case R.id.BtnLogin:
                UserLogin();
                break;
        }
    }
}
