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

/**
 * SignupActivity.
 * Created by dgalindo on 10/03/18.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SIGNUP_TAG";
    private EditText et_newUsername;
    private EditText et_newName;
    private EditText et_signupEmail;
    private EditText et_newPassword;
    private EditText et_retypePass;
    private CheckBox cb_isAdmin;
    private boolean isTaken;
    private boolean allOK = true;

    private DatabaseReference dbReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_newUsername = findViewById(R.id.etUsername);
        et_newName = findViewById(R.id.etName);
        et_signupEmail = findViewById(R.id.etSignupEmail);
        et_newPassword = findViewById(R.id.etSignupPassword);
        et_retypePass = findViewById(R.id.etRetypePass);
        cb_isAdmin = findViewById(R.id.cbAdminAccount);

        dbReference = FirebaseDatabase.getInstance().getReference();

        Button btn_signup = findViewById(R.id.btnSignup);
        btn_signup.setOnClickListener(this);

    }


    /**
     * On Sign up button click.
     */
    @Override
    public void onClick(View view) {

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String email = et_signupEmail.getText().toString();
        String password = Hash.sha1(et_newPassword.getText().toString());

        if (validateAll()) {
            authenticateNewUser(email, password);
        }

    }


    /**
     * Validate if every field on the activity is correct.
     * @return boolean value if everything is ok.
     */
    private boolean validateAll() {

        //Check if the EditTexts are empty. If true: show toast.
        if (TextUtils.isEmpty(et_signupEmail.getText().toString())
                || TextUtils.isEmpty(et_newPassword.getText().toString())
                || TextUtils.isEmpty(et_retypePass.getText().toString())
                || TextUtils.isEmpty(et_newUsername.getText())
                || TextUtils.isEmpty(et_newName.getText())) {

            Toast.makeText(this, "You MUST fill every field to continue!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return false;
        }

        //Check if password have more than 6 chars (Firebase rule).
        if (et_newPassword.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must have more than 6 characters!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return false;
        }

        //Confirms if the EditTexts of the passwords are correct. If false: show toast.
        if (!TextUtils.equals(et_newPassword.getText().toString(), et_retypePass.getText().toString())) {
            Toast.makeText(this, "Both password fields MUST match", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return false;
        }

        //Validate password
        if(!validatePass(et_newPassword.getText().toString())) {
            Toast.makeText(this, "Password must have at least one capital letter, one special character and one number", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return false;
        }

        if (doesNameExist(et_newUsername.getText().toString())) {
            Toast.makeText(this, "That Username is already taken!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
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
            if(password.charAt(i) > 47 && password.charAt(i) < 58) x++;
            if(password.charAt(i) > 32 && password.charAt(i) < 48) x++;
        }

        if(x == 3) bol = true;

        return bol;
    }


    /**
     * Creates an user authentication in Firebase.
     * @param email New user email.
     * @param password New user password.
     */
    private void authenticateNewUser(final String email, final String password) {
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


    /**
     * Insert new user to Firebase db.
     * @param email new email.
     * @param password new password.
     */
    private void insertNewUserToDB(String email, String password) {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String UUID;

        assert currentFirebaseUser != null;
        UUID = currentFirebaseUser.getUid();

        Log.d("UUID_TAG", "THE UUID >> " + UUID);

        //creates new userId for firebase.
        String userId = UUID;
        //creating user object.
        User user = new User(email, et_newUsername.getText().toString(), password, et_newName.getText().toString(),
                "example_birth", cb_isAdmin.isChecked());


        if (allOK) {
            //pushing user to 'users' node using the userId.
            mDatabase.child(userId).setValue(user);
            registerUsername();

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Signup successfull!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        }
    }


    /**
     * Register the new User username to a table named "usernames"
     * to validate in a future if a new username has this username.
     */
    private void registerUsername() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference takenUserNames = database.getReference("usernames");
        takenUserNames.child(et_newUsername.getText().toString()).setValue(true);
    }


    /**
     * Check if the username already exist.
     * @param s is the username to validate.
     * @return boolean value: true if is already taken; false if not.
     */
    private boolean doesNameExist(final String s) {
        DatabaseReference theTakenNameRef = dbReference.getRef().child("usernames");
        theTakenNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isTaken = dataSnapshot.hasChild(s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Enter a valid Username!", Toast.LENGTH_SHORT).show();
                isTaken = true;
                allOK = false;
            }
        });

        return isTaken;
    }

}
