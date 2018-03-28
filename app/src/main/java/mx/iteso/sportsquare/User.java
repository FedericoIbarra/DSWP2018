package mx.iteso.sportsquare;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * User Model.
 * Created by dgalindo on 10/03/18.
 */
@IgnoreExtraProperties
public class User {

    public String uuid;
    public String username;
    public String password;
    public String fullName;
    public String email;
    public String dateOfBirth;
    public boolean isAdmin;

    public User() {

    }

    public User(String uuid, String email, String username,
                String password, String fullName, String dateOfBirth, boolean isAdmin) {

        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.isAdmin = isAdmin;
        this.email = email;
    }

}
