package com.cloutgang.proofofconcept2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LobbyScreen extends AppCompatActivity {

    String lobbyId;
    Lobby lobby;
    DatabaseReference roomRef;
    boolean owner;
    private FusedLocationProviderClient client;
    TextView txtMeal;
    TextView txtOwner;
    TextView txtPrice;
    TextView txtIngredient;
    TextView txtDistance;
    ScrollView guestScroll;
    LinearLayout guestLayout;

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);

        txtMeal = findViewById(R.id.txtMeal);
        txtOwner = findViewById(R.id.txtOwner);
        txtPrice = findViewById(R.id.txtPrice);
        txtIngredient = findViewById(R.id.txtIngredients);
        txtDistance = findViewById(R.id.txtDistance);
        guestScroll = findViewById(R.id.GuestScrollView);
        guestLayout = findViewById(R.id.GuestList);

        //toolbar stuff
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FeedMe");


        Bundle b = getIntent().getExtras();

        if (b != null) {
            lobbyId = b.getCharSequence("key").toString();
            owner = b.getBoolean("owner");
        }

        roomRef = FirebaseDatabase.getInstance().getReference("Rooms/" + lobbyId);

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lobby = dataSnapshot.getValue(Lobby.class);

                try {
                    txtMeal.setText(lobby.meal);
                    txtOwner.setText(lobby.ownerName);
                    txtPrice.setText("Price: " + lobby.price);
                    txtIngredient.setText("Ingredients: " + lobby.ingredients);

                    String locationString = lobby.location;
                    String longtitude = locationString.split(" ")[0];
                    String latitude = locationString.split(" ")[1];

                    final Location lobbyLocation = new Location("");
                    lobbyLocation.setLongitude(Double.parseDouble(longtitude));
                    lobbyLocation.setLatitude(Double.parseDouble(latitude));

                    Geocoder geocoder = new Geocoder(LobbyScreen.this, Locale.getDefault());

                    List<Address> adresses;

                    adresses = geocoder.getFromLocation(lobbyLocation.getLatitude(), lobbyLocation.getLongitude(), 1);

                    if (adresses != null){
                        ((TextView)findViewById(R.id.txtLocation)).setText(adresses.get(0).getPostalCode());
                    }

                    if (ActivityCompat.checkSelfPermission(LobbyScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    client.getLastLocation().addOnSuccessListener(LobbyScreen.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                float distanceToLobby = location.distanceTo(lobbyLocation);
                                distanceToLobby = distanceToLobby / 1000;
                                distanceToLobby = Math.round(distanceToLobby);
                                txtDistance.setText("" + distanceToLobby + "  km");
                            }
                        }
                    });


                    fillGuestList();
                }
                catch (Exception e){
                    Log.e("HELPME", e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void fillGuestList()
    {
        // Remove all previous views
        guestLayout.removeAllViewsInLayout();

        try{
            // Loop through all names in collection
            for (String guestName : lobby.guestIDs){
                Log.i("HELPME", guestName);
                TextView text = new TextView(this);
                text.setText(guestName);
                text.setTextColor(Color.BLACK);
                guestLayout.addView(text);
            }
        }
        catch (NullPointerException e){
            // This only happens if user creates a room.
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (owner){

            roomRef.removeValue();

            Intent intent = new Intent(LobbyScreen.this, LobbyList.class);
            startActivity(intent);
        }
        else{
            try{
                if (lobby.guestIDs.contains(user.getDisplayName())){
                    lobby.guestIDs.remove(user.getDisplayName());
                    roomRef.setValue(lobby);
                    Toast.makeText(LobbyScreen.this, "Removed user from list", Toast.LENGTH_SHORT);
                }
            }
            catch (Exception e){

            }
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
            case R.id.menuProfile:

                finish();
                startActivity(new Intent(this, ProfileScreen.class));
                break;

            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginScreen.class));

                break;
        }
        return true;
    }
}
