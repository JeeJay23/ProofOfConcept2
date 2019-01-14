package com.cloutgang.proofofconcept2;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jvjad on 09-Jan-19.
 */

public class Lobby {

    public String owner;
    public String ownerName;

    // exclude tag causes id not to be saved in the database
    @Exclude
    public String id;

    public List<String> guestIDs;
    public String meal;
    public String price;
    public String ingredients;
    public String location;
    public int maxGuests;
    public String date;

    public Lobby (String owner, String ownerName, String meal, String price, String ingredients, String date, String location, int maxGuests)
    {
        this.owner = owner;
        this.ownerName = ownerName;
        this.meal = meal;
        this.price = price;
        this.ingredients = ingredients;
        this.date = date;
        this.location = location;
        this.maxGuests = maxGuests;
    }

    public Lobby ()
    {
        // empty constructor required for saving class in firebase
    }

    public void addGuest(String guestId)
    {
        if (guestIDs == null){
            guestIDs = new ArrayList<>();
        }

        guestIDs.add(guestId);
    }
}
