package mx.iteso.sportsquare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *
 * Created by dgalindo on 10/03/18.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText newEmailET;
    private EditText newPasswordET;
    private EditText retypePassET;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        newEmailET = findViewById(R.id.etSignupEmail);
        newPasswordET = findViewById(R.id.etSignupPassword);
        retypePassET = findViewById(R.id.etRetypePass);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button signupBtn = findViewById(R.id.btnSignup);
        signupBtn.setOnClickListener(this);

    }


    //Click listener.
    @Override
    public void onClick(View view) {

        //Check if the EditTexts are empty. If true: show toast.
        if (TextUtils.isEmpty(newEmailET.getText().toString())
                || TextUtils.isEmpty(newPasswordET.getText().toString())
                || TextUtils.isEmpty(retypePassET.getText().toString())) {

            Toast.makeText(this, "You MUST fill every field to continue!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Confirms if the EditTexts of the passwords are correct. If false: show toast.
        if (!TextUtils.equals(newPasswordET.getText().toString(), retypePassET.getText().toString())) {
            Toast.makeText(this, "Both password fields MUST match", Toast.LENGTH_SHORT).show();
            return;
        }


        String email = newEmailET.getText().toString();
        String password = Hash.sha1(newPasswordET.getText().toString());

        //Validate password
        if(!validatePass(newPasswordET.getText().toString())) {
            Toast.makeText(this, "Password must have at least one capital letter, one special character and one number", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if password have more than 6 chars (Firebase rule).
        if (newPasswordET.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must have more than 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }





        //Authenticates user with email and password.
        authenticateNewUser(email, password);

    }

    /**
     * Validate Password.
     * At least one capital letter, one number and one special character.
     * @param password a string with the password.
     * */

    boolean validatePass(String password) {
        boolean bol = false;
        int i, x = 0;

        for(i = 0; i < password.length(); i++) {
            if(password.charAt(i) > 64 && password.charAt(i) < 91) x++;
            if(password.charAt(i) > 47 && password.charAt(i) < 58) x++;
            if(password.charAt(i) > 32 && password.charAt(i) < 48) x++;
        }

        if(x == 3) bol = true;

        return bol;
    }

    //Creates an user authentication in Firebase.
    private void authenticateNewUser(final String email, final String password) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH_LOG", "Authentication successful");
                            //insertNewUserToDB(email, password);

                            //Start LoginActivity.
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();

                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Save a user into the Firebase Database.
    //TODO: FIRST it must be created some rule to link user auth UUID to the new User UUID.
    private void insertNewUserToDB(String email, String password) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //creates new userId for firebase.
        String userId = mDatabase.push().getKey();
        //creating user object.
        User user = new User(email,"prueba123", password, Hash.sha1("Diego"),
                "Galindo", "14-02-92", true);

        //pushing user to 'users' node using the userId.
        mDatabase.child(userId).setValue(user);
        Toast.makeText(this, "Signup successfull!!", Toast.LENGTH_SHORT).show();
    }

}
