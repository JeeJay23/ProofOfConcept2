package com.cloutgang.proofofconcept2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class CreateLobby extends AppCompatActivity {
    private FusedLocationProviderClient client;
    FirebaseAuth mAuth;
    EditText txtMealName, txtMealPrice, txtMealIngredient, txtMealMaxGuests, txtMealLocation;
    ProgressBar progressBar;
    String locationString;
    Lobby createdLobby;


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

    }
    Calendar date;
    int year;
    int monthOfYear;
    int dayOfMonth;
    int hourOfDay;
    int minute;
    public void showDateTimePicker(View view) {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(CreateLobby.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int Hyear, final int HmonthOfYear, final int HdayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(CreateLobby.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int HhourOfDay, int Hminute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        year = Hyear;
                        monthOfYear = HmonthOfYear;
                        dayOfMonth = HdayOfMonth;
                        hourOfDay = HhourOfDay;
                        minute = Hminute;

                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    public void submitLobby(View view) {
        registerUser();
    }

    private void registerUser() {

        final String mealName = txtMealName.getText().toString().trim();
        final String mealPrice = txtMealPrice.getText().toString().trim();
        final String mealIngredients = txtMealIngredient.getText().toString().trim();
        String maxGuestsS = txtMealMaxGuests.getText().toString().trim();
        boolean correctGuests = true;

        int maxGuests = 0;
        try {
            maxGuests = Integer.parseInt(maxGuestsS);
        } catch (NumberFormatException nfe) {
            correctGuests = false;
        }

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
            txtMealMaxGuests.setError("Incorrect max guests");
            txtMealMaxGuests.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        java.util.Date c = java.util.Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/mm-hh:ss");
        final String formattedDate = df.format(c);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference lobbyRef = FirebaseDatabase.getInstance().getReference("Rooms");
        final DatabaseReference roomRef = lobbyRef.push();

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final int finalMaxGuests = maxGuests;
        client.getLastLocation().addOnSuccessListener(CreateLobby.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    String dateFormat = "" + year + "/" + monthOfYear + "/" + dayOfMonth + "/" + hourOfDay + "/" + minute;
                    locationString = "" + location.getLongitude() + " " + location.getLatitude();
                    createdLobby = new Lobby(user.getUid(), user.getDisplayName(), mealName, mealPrice, mealIngredients, dateFormat, locationString, finalMaxGuests);
                    createdLobby.id = roomRef.getKey();

                    roomRef.setValue(createdLobby)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CreateLobby.this, "Data sent successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(CreateLobby.this, LobbyScreen.class);

                                    Bundle b = new Bundle();
                                    b.putCharSequence("key", createdLobby.id);
                                    b.putBoolean("owner", true);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateLobby.this, "Data failed to send", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //locationString = mealLocation;
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
