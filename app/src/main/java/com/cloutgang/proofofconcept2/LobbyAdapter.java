package com.cloutgang.proofofconcept2;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by jvjad on 12-Jan-19.
 */

public class LobbyAdapter extends ArrayAdapter<Lobby> {
    private Activity context;
    private List<Lobby> lobbyList;
    private Location userLocation;

    public LobbyAdapter (Activity context, List<Lobby> lobbyList, Location location){
        super(context, R.layout.list_view, lobbyList);
        this.context = context;
        this.lobbyList = lobbyList;
        this.userLocation = location;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.list_view, null, true);

        TextView mealName = listView.findViewById(R.id.txtMealName);
        mealName.setTextColor(Color.BLACK);
        TextView lobbyOwner = listView.findViewById(R.id.txtOwnername);
        lobbyOwner.setTextColor(Color.BLACK);
        TextView guestCount = listView.findViewById(R.id.txtGuestCount);
        TextView txtDistance = listView.findViewById(R.id.txtDistance);

        Lobby lobby = lobbyList.get(position);
        mealName.setText(lobby.meal);
        lobbyOwner.setText(lobby.ownerName);

        String lobbyString = lobby.location;
        String lobbyLongtitude = lobby.location.split(" ")[0];
        String lobbyLatitude = lobby.location.split(" ")[1];
        Location lobbyLocation = new Location("");
        lobbyLocation.setLongitude(Double.parseDouble(lobbyLongtitude));
        lobbyLocation.setLatitude(Double.parseDouble(lobbyLatitude));
        float distance = Math.round(userLocation.distanceTo(lobbyLocation)/1000);

        txtDistance.setTextColor(Color.BLACK);
        txtDistance.setText("" + distance + "  km");
        try{
            guestCount.setText(lobby.guestIDs.size() + "/" + lobby.maxGuests + " guests");
            guestCount.setTextColor(Color.BLACK);
        }
        catch(Exception e){
            guestCount.setText("0/" + lobby.maxGuests + " guests");
            guestCount.setTextColor(Color.BLACK);
        }

        return listView;
    }
}
