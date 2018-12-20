package com.cloutgang.proofofconcept2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LobbyScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);
    }

    public void AddTextView(View view)
    {
        LinearLayout layout = findViewById(R.id.linearLayout);

        TextView txtView = new TextView(this);
        txtView.setText("This is a test view");

        layout.addView(txtView);
    }
}
