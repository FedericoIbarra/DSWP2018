package mx.iteso.sportsquare.beans;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Enrique on 3/26/2018.
 */

@IgnoreExtraProperties
public class Establishment {
    public String username;
    public String password;
    public String name;
    public String location;
    public String email;
    public String sports;
    public String schedule;

    public Establishment(String email, String username, String password, String name, String location, String schedule, String sports) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.location = location;
        this.email = email;
        this.schedule = schedule;
        this.sports = sports;
    }

}
