package mx.iteso.sportsquare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.sportsquare.beans.User;
import mx.iteso.sportsquare.util.Hash;

/**
 * Created by dgalindo on 10/03/18.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SIGNUP_TAG";
    private EditText newUsername, newName, newEmailET, newPasswordET, retypePassET;
    private CheckBox cbIsAdmin;
    private boolean isTaken;
    private boolean allOK = true;

    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        newUsername = findViewById(R.id.etUsername);
        newName = findViewById(R.id.etName);
        newEmailET = findViewById(R.id.etSignupEmail);
        newPasswordET = findViewById(R.id.etSignupPassword);
        retypePassET = findViewById(R.id.etRetypePass);
        cbIsAdmin = findViewById(R.id.cbAdminAccount);

        dbReference = FirebaseDatabase.getInstance().getReference();

        Button signupBtn = findViewById(R.id.btnSignup);
        signupBtn.setOnClickListener(this);

    }

    protected boolean isEmailValid(String email){
        //TODO
        return true;
    }

    //Click listener.
    @Override
    public void onClick(View view) {
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Check if the EditTexts are empty. If true: show toast.
        if (TextUtils.isEmpty(newEmailET.getText().toString())
                || TextUtils.isEmpty(newPasswordET.getText().toString())
                || TextUtils.isEmpty(retypePassET.getText().toString())
                || TextUtils.isEmpty(newUsername.getText())
                || TextUtils.isEmpty(newName.getText())) {

            Toast.makeText(this, "You MUST fill every field to continue!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        //Check if password have more than 6 chars (Firebase rule).
        if (newPasswordET.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must have more than 6 characters!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        //Confirms if the EditTexts of the passwords are correct. If false: show toast.
        if (!TextUtils.equals(newPasswordET.getText().toString(), retypePassET.getText().toString())) {
            Toast.makeText(this, "Both password fields MUST match", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

/*        //Validate password
       if(!validatePass(newPasswordET.getText().toString())) {
            Toast.makeText(this, "Password must have at least one capital letter, one special character and one number", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }//*/

        String email = newEmailET.getText().toString();
        String password = Hash.sha1(newPasswordET.getText().toString());

        //First check if username exists.
        if (doesNameExist(newUsername.getText().toString())) {
            Toast.makeText(this, "That Username is already taken!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            allOK = false;
        } else {
            //Authenticates user with email and password.
            authenticateNewUser(email, password);
        }


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
            else if (password.charAt(i) > 47 && password.charAt(i) < 58) x++;
            else if (password.charAt(i) > 32 && password.charAt(i) < 48) x++;
        }

        if(x == 3) bol = true;

        return bol;
    }

    //Creates an user authentication in Firebase.
    //qwertyA1.

    protected void authenticateNewUser(final String email, final String password) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Authentication successful");

                            insertNewUserToDB(email, password);


                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            allOK = false;
                        }
                    }
                });
    }

    //Save a user into the Firebase Database.
    private void insertNewUserToDB(String email, String password) {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String UUID;

        assert currentFirebaseUser != null;
        UUID = currentFirebaseUser.getUid();

        Log.d("UUID_TAG", "THE UUID >> " + UUID);

        //creates new userId for firebase.
        String userId = UUID;
        //creating User object.
        User user = new User(email, newUsername.getText().toString(), password, newName.getText().toString(),
                "example_birth", cbIsAdmin.isChecked());


        if (allOK) {
            //pushing User to 'users' node using the userId.
            mDatabase.child(userId).setValue(user);
            registerUsername();

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Signup successfull!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void registerUsername() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference takenUserNames = database.getReference("usernames");

        takenUserNames.child(newUsername.getText().toString()).setValue(true);
    }

    public boolean doesNameExist(final String sUsername) {
        DatabaseReference theTakenNameRef = dbReference.getRef().child("usernames");
        theTakenNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(sUsername)) {
                    isTaken = true;
                } else {
                    isTaken = false;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),
                        "Enter a valid Username!", Toast.LENGTH_SHORT).show();
                isTaken = true;
                allOK = false;
            }
        });

        return isTaken;
    }



}
