package com.cloutgang.proofofconcept2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LobbyScreen extends AppCompatActivity {

    String lobbyId;
    Lobby lobby;
    DatabaseReference roomRef;

    TextView txtMeal;
    TextView txtOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);

        txtMeal = findViewById(R.id.txtMeal);
        txtOwner = findViewById(R.id.txtOwner);

        Bundle b = getIntent().getExtras();

        if (b != null){
            lobbyId = b.getCharSequence("key").toString();
        }

        Log.i("HELPME", lobbyId);

        roomRef = FirebaseDatabase.getInstance().getReference("Rooms/" + lobbyId);
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lobby = dataSnapshot.getValue(Lobby.class);

                try{
                    txtMeal.setText(lobby.meal);
                    txtOwner.setText(lobby.ownerName);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (lobby.guestIDs.contains(user.getDisplayName())){
            lobby.guestIDs.remove(user.getDisplayName());
            roomRef.setValue(lobby);
            Toast.makeText(LobbyScreen.this, "Removed user from list", Toast.LENGTH_SHORT);
        }
    }
}
