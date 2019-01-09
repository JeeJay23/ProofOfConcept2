package com.cloutgang.proofofconcept2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.text.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateLobby extends AppCompatActivity {

    EditText txtMealName, txtMealPrice, txtMealIngredient, txtMealMaxGuests, txtMealLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);


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
        }

        if (mealPrice.isEmpty())
        {
            txtMealPrice.setError("No price set");
            txtMealPrice.requestFocus();
        }

        if (maxGuests == 0)
        {
            txtMealMaxGuests.setError("Need at least 1 guest");
            txtMealMaxGuests.requestFocus();
        }

        if (mealLocation.isEmpty())
        {
            txtMealLocation.setError("No location set");
            txtMealLocation.requestFocus();
        }

        // no idea how to get id, fix later

        java.util.Date c = java.util.Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Lobby lobby = new Lobby("no owner logic yet", mealName, mealPrice, mealIngredients, formattedDate, "no location yet", maxGuests );

        DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference("Rooms");
        lobbyRef.push().setValue(lobby);
    }
}
