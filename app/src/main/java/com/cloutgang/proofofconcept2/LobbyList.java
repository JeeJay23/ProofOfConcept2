package com.cloutgang.proofofconcept2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LobbyList extends AppCompatActivity {

    DatabaseReference roomsRef;
    ListView listView;
    List<Lobby> lobbyList;
    ImageButton imageButton;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_list);

        // List stuff
        listView = findViewById(R.id.List_View);
        roomsRef = FirebaseDatabase.getInstance().getReference("Rooms");

        // Toolbar stuff
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FeedMe");
        lobbyList = new ArrayList<>();

        //floatbutton stuff
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyList.this, CreateLobby.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        client = LocationServices.getFusedLocationProviderClient(this);

        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lobbyList.clear();

                for (DataSnapshot lobbySnap : dataSnapshot.getChildren()) {

                    Lobby lobby = lobbySnap.getValue(Lobby.class);
                    lobby.id = lobbySnap.getKey();
                    lobbyList.add(lobby);
                }
                if (ActivityCompat.checkSelfPermission(LobbyList.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(LobbyList.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LobbyAdapter lobbyAdapter = new LobbyAdapter(LobbyList.this, lobbyList, location);
                            listView.setAdapter(lobbyAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Lobby lobby = lobbyList.get(i);

                                    try {
                                        if (lobby.guestIDs.size() >= lobby.maxGuests) {
                                            Toast.makeText(LobbyList.this, "Room is already full", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    } catch (Exception e) {

                                    }

                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    final FirebaseUser user = mAuth.getCurrentUser();

                                    lobby.addGuest(user.getDisplayName());

                                    roomsRef.child(lobby.id).setValue(lobby);

                                    Intent intent = new Intent(LobbyList.this, LobbyScreen.class);
                                    Bundle b = new Bundle();
                                    b.putCharSequence("key", lobby.id);
                                    b.putBoolean("owner", false);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                startActivity(new Intent(this, ProfileScreen.class));
                break;

        }
        return true;
    }
}
