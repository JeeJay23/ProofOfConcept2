package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    boolean owner;

    TextView txtMeal;
    TextView txtOwner;
    TextView txtPrice;
    TextView txtIngredient;
    ScrollView guestScroll;
    LinearLayout guestLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);

        txtMeal = findViewById(R.id.txtMeal);
        txtOwner = findViewById(R.id.txtOwner);
        txtPrice = findViewById(R.id.txtPrice);
        txtIngredient = findViewById(R.id.txtIngredients);
        guestScroll = findViewById(R.id.GuestScrollView);
        guestLayout = findViewById(R.id.GuestList);

        Bundle b = getIntent().getExtras();

        if (b != null){
            lobbyId = b.getCharSequence("key").toString();
            owner = b.getBoolean("owner");
        }

        roomRef = FirebaseDatabase.getInstance().getReference("Rooms/" + lobbyId);

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lobby = dataSnapshot.getValue(Lobby.class);

                try{
                    txtMeal.setText(lobby.meal);
                    txtOwner.setText(lobby.ownerName);
                    txtPrice.setText("Price: " + lobby.price);
                    txtIngredient.setText("Ingredients: " + lobby.ingredients);

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

            if (lobby.guestIDs.contains(user.getDisplayName())){
                lobby.guestIDs.remove(user.getDisplayName());
                roomRef.setValue(lobby);
                Toast.makeText(LobbyScreen.this, "Removed user from list", Toast.LENGTH_SHORT);
            }
        }
    }
}
