package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenuScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FeedMe");

        findViewById(R.id.BtnToLoginScreen).setOnClickListener(this);
        findViewById(R.id.BtnToLobbyScreen).setOnClickListener(this);
        findViewById(R.id.btnToProfileScreen).setOnClickListener(this);
        findViewById(R.id.btnToCreateLobbyScreen).setOnClickListener(this);
    }

    //when cicked on a button go to that activity
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //when clicked on loginscreen
            case R.id.BtnToLoginScreen:
                startActivity(new Intent(this, LoginScreen.class));
                break;

            //when clicked on lobbyscreen
            case R.id.BtnToLobbyScreen:
                startActivity(new Intent(this, LobbyScreen.class));
                break;

            //when clicked on profilescreen
            case R.id.btnToProfileScreen:
                startActivity(new Intent(this, ProfileScreen.class));
                break;

            case R.id.btnToCreateLobbyScreen:
                startActivity(new Intent(this, CreateLobby.class));
                break;
        }
    }


    //run this when the Menu for the logout button is created (see app/res/menu)
    //this adds the menu to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }


    //this happens when you click on the options icon in the menu
    //for now we only have the logout case (we could add more in the future)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginScreen.class));
                break;

            case R.id.menuProfile:
                finish();
                startActivity(new Intent(this, ProfileScreen.class));
                break;

        }
        return true;
    }

}
