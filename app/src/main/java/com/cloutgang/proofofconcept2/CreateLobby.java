package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CreateLobby extends AppCompatActivity {
    private FusedLocationProviderClient client;
    FirebaseAuth mAuth;
    EditText txtMealName, txtMealPrice, txtMealIngredient, txtMealMaxGuests, txtMealLocation;
    ProgressBar progressBar;
    String locationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);
        requestPermission();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getSupportActionBar().setTitle("FeedMe");
        txtMealName = findViewById(R.id.txtxName);
        txtMealPrice = findViewById(R.id.txtPrice);
        txtMealIngredient = findViewById(R.id.txtIngredients);
        txtMealMaxGuests = findViewById(R.id.txtMaxGuests);
        txtMealLocation = findViewById(R.id.txtLocation);
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    public void submitLobby(View view) {
        registerUser();
    }

    private void registerUser() {
        String mealName = txtMealName.getText().toString().trim();
        String mealPrice = txtMealPrice.getText().toString().trim();
        String mealIngredients = txtMealIngredient.getText().toString().trim();
        String maxGuestsS = txtMealMaxGuests.getText().toString().trim();
        int maxGuests = Integer.parseInt(maxGuestsS);
        String mealLocation = txtMealLocation.getText().toString().trim();

        if (mealName.isEmpty()) {
            txtMealName.setError("No name set");
            txtMealName.requestFocus();
            return;
        }

        if (mealPrice.isEmpty()) {
            txtMealPrice.setError("No price set");
            txtMealPrice.requestFocus();
            return;
        }

        if (maxGuests == 0) {
            txtMealMaxGuests.setError("Need at least 1 guest");
            txtMealMaxGuests.requestFocus();
            return;
        }

        if (mealLocation.isEmpty()) {
            txtMealLocation.setError("No location set");
            txtMealLocation.requestFocus();
            return;
        }

        // no idea how to get id, fix later

        progressBar.setVisibility(View.VISIBLE);

        java.util.Date c = java.util.Calendar.getInstance().getTime();
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(CreateLobby.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    locationString = location.toString();
                }
            }
        });
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Lobby lobby = new Lobby(user.getDisplayName(), mealName, mealPrice, mealIngredients, formattedDate, locationString, maxGuests );

        DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference("Rooms");
        DatabaseReference roomRef = lobbyRef.push();
        roomRef.setValue(lobby)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateLobby.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateLobby.this, "Data failed to send", Toast.LENGTH_SHORT).show();
                    }
                });

        progressBar.setVisibility(View.GONE);
    }
    private void requestPermission () {
        ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 1);
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
