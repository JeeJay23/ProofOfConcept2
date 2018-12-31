package com.cloutgang.proofofconcept2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

public class ProfileScreen extends AppCompatActivity {


    ImageView imageView;
    EditText editText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        editText = (EditText) findViewById(R.id.editTextDisplayName);


    }
}
