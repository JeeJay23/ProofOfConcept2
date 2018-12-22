package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Button BtnToLoginScreen = (Button)findViewById(R.id.BtnToLoginScreen);
        BtnToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), LoginScreen.class);
                //je kan ook info doorgeven
                startActivity(startIntent);

            }
        });

        Button BtnToLobbyScreen = (Button)findViewById(R.id.BtnToLobbyScreen);
        BtnToLobbyScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), LobbyScreen.class);
                //je kan ook info doorgeven
                startActivity(startIntent);

            }
        });


    }
}
