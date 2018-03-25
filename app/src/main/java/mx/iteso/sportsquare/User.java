package mx.iteso.sportsquare;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * User Model.
 * Created by dgalindo on 10/03/18.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public String dateOfBirth;
    public boolean isAdmin;

    public User() {

    }

    public User(String email, String username, String password, String firstName, String lastName, String dateOfBirth, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.isAdmin = isAdmin;
        this.email = email;
    }

}