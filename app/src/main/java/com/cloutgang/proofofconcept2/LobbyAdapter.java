package com.cloutgang.proofofconcept2;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jvjad on 12-Jan-19.
 */

public class LobbyAdapter extends ArrayAdapter<Lobby> {
    private Activity context;
    private List<Lobby> lobbyList;

    public LobbyAdapter (Activity context, List<Lobby> lobbyList){
        super(context, R.layout.list_view, lobbyList);
        this.context = context;
        this.lobbyList = lobbyList;
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


        Lobby lobby = lobbyList.get(position);
        mealName.setText(lobby.meal);
        lobbyOwner.setText(lobby.ownerName);

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
