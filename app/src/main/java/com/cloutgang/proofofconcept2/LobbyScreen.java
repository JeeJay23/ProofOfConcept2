package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LobbyScreen extends AppCompatActivity {

    DatabaseReference roomsRef;
    ListView listView;
    List<Lobby> lobbyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);


        // List stuff
        listView = findViewById(R.id.List_View);
        roomsRef = FirebaseDatabase.getInstance().getReference("Rooms");


        // Toolbar stuff
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FeedMe");
        lobbyList = new ArrayList<>();

        // stuff that doesn't work
//        ValueEventListener roomListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(LobbyScreen.this, "data changed", Toast.LENGTH_LONG).show();
//                Lobby lobby = dataSnapshot.getValue(Lobby.class);
//                addTextView(lobby.meal);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot lobbySnap : dataSnapshot.getChildren()){

                    Lobby lobby = lobbySnap.getValue(Lobby.class);
                    lobbyList.add(lobby);
                }

                LobbyAdapter lobbyAdapter = new LobbyAdapter(LobbyScreen.this, lobbyList);
                listView.setAdapter(lobbyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public void addTextView(View view)
//    {
//        LinearLayout layout = findViewById(R.id.linearLayout);
//
//        TextView txtView = new TextView(this);
//        txtView.setText("This is a test view");
//
//        Toast.makeText(LobbyScreen.this, roomsRef.toString(), Toast.LENGTH_LONG).show();
//
//        layout.addView(txtView);
//    }

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
