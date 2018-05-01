package mx.iteso.sportsquare;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * User Model.
 * Created by dgalindo on 10/03/18.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String password;
    public String fullName;
    public String email;
    public String dateOfBirth;
    public boolean isAdmin;

    public User() { }

    /** User object for Firebase Database.
     *
     * @param email User email.
     * @param username User username.
     * @param password User password.
     * @param fullName User full name.
     * @param dateOfBirth User date of birth.
     * @param isAdmin User that says if is administrator.
     */
    public User(String email, String username,
                String password, String fullName, String dateOfBirth, boolean isAdmin) {

        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.isAdmin = isAdmin;
        this.email = email;
    }

}
