/**
 * Created by jvjad on 09-Jan-19.
 */

public class Lobby {

    public String owner;

    public String meal;
    public String price;
    public String ingredients;
    public String date;
    public String location;
    public int maxGuests;

    public Lobby (String owner, String meal, String price, String ingredients, String date, String location, int maxGuests)
    {
        this.owner = owner;
        this.meal = meal;
        this.price = price;
        this.ingredients = ingredients;
        this.date = date;
        this.location = location;
        this.maxGuests = maxGuests;
    }
}
