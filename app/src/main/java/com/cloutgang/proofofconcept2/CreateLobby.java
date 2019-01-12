package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class CreateLobby extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText txtMealName, txtMealPrice, txtMealIngredient, txtMealMaxGuests, txtMealLocation;
    LocationListener locationListener;
    LocationManager locationManager;
    String locationString;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getSupportActionBar().setTitle("FeedMe");



        txtMealName = findViewById(R.id.txtxName);
        txtMealPrice = findViewById(R.id.txtPrice);
        txtMealIngredient = findViewById(R.id.txtIngredients);
        txtMealMaxGuests = findViewById(R.id.txtMaxGuests);
        txtMealLocation = findViewById(R.id.txtLocation);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationString = "/n" + location.getLongitude() + " " + location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
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

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 0, 1000, locationListener);
        Lobby lobby = new Lobby(user.getDisplayName(), mealName, mealPrice, mealIngredients, formattedDate, locationString, maxGuests );

        DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference("Rooms");
        DatabaseReference roomRef = lobbyRef.push();
        roomRef.setValue(lobby);

        progressBar.setVisibility(View.GONE);
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
