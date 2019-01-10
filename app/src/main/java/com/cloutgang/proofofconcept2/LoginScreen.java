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
        //trim removes the spaces at the beginning and the end
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

        //try to sign in
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //when successful finish this activity and go to the profile activity
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginScreen.this, CreateLobby.class);

                    //make sure the user can't go back to the login activity with the back button
                    //this way the user has to make use of the logout button to return
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //see if the user is already logged in when starting the activity
    @Override
    protected void onStart() {
        super.onStart();

        //when the user is already logged in go to the profile activity
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, CreateLobby.class));
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
