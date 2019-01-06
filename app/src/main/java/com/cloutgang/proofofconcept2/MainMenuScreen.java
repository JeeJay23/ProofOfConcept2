package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainMenuScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findViewById(R.id.BtnToLoginScreen).setOnClickListener(this);
        findViewById(R.id.BtnToLobbyScreen).setOnClickListener(this);
        findViewById(R.id.BtnToProfileScreen).setOnClickListener(this);
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
            case R.id.BtnToProfileScreen:
                startActivity(new Intent(this, ProfileScreen.class));
                break;
        }
    }
}
