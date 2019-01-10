package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class CreateLobby extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText txtMealName, txtMealPrice, txtMealIngredient, txtMealMaxGuests, txtMealLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtMealName = findViewById(R.id.txtxName);
        txtMealPrice = findViewById(R.id.txtPrice);
        txtMealIngredient = findViewById(R.id.txtIngredients);
        txtMealMaxGuests = findViewById(R.id.txtMaxGuests);
        txtMealLocation = findViewById(R.id.txtLocation);
    }

    public void SubmitLobby(View view)
    {
        RegisterUser();
    }

    private void RegisterUser()
    {
        String mealName = txtMealName.getText().toString().trim();
        String mealPrice = txtMealPrice.getText().toString().trim();
        String mealIngredients = txtMealIngredient.getText().toString().trim();
        String maxGuestsS = txtMealMaxGuests.getText().toString().trim();
        int maxGuests = Integer.parseInt(maxGuestsS);
        String mealLocation = txtMealLocation.getText().toString().trim();

        if (mealName.isEmpty())
        {
            txtMealName.setError("No name set");
            txtMealName.requestFocus();
            return;
        }

        if (mealPrice.isEmpty())
        {
            txtMealPrice.setError("No price set");
            txtMealPrice.requestFocus();
            return;
        }

        if (maxGuests == 0)
        {
            txtMealMaxGuests.setError("Need at least 1 guest");
            txtMealMaxGuests.requestFocus();
            return;
        }

        if (mealLocation.isEmpty())
        {
            txtMealLocation.setError("No location set");
            txtMealLocation.requestFocus();
            return;
        }

        // no idea how to get id, fix later

        java.util.Date c = java.util.Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy-hh:mm");
        String formattedDate = df.format(c);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Lobby lobby = new Lobby(user.getDisplayName(), mealName, mealPrice, mealIngredients, formattedDate, mealLocation, maxGuests );

        DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference("Rooms");
        DatabaseReference roomRef = lobbyRef.push();
        roomRef.setValue(lobby);
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
