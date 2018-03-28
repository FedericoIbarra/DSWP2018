package mx.iteso.sportsquare;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *
 * Created by dgalindo on 10/03/18.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SIGNUP_TAG";
    private EditText newUsername, newName, newEmailET, newPasswordET, retypePassET;
    private CheckBox cbIsAdmin;

    private DatabaseReference mDatabase;
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

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button signupBtn = findViewById(R.id.btnSignup);
        signupBtn.setOnClickListener(this);

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
            return;
        }

        //Confirms if the EditTexts of the passwords are correct. If false: show toast.
        if (!TextUtils.equals(newPasswordET.getText().toString(), retypePassET.getText().toString())) {
            Toast.makeText(this, "Both password fields MUST match", Toast.LENGTH_SHORT).show();
            return;
        }


        String email = newEmailET.getText().toString();
        String password = newPasswordET.getText().toString();

        //Check if password have more than 6 chars (Firebase rule).
        if (password.length() < 6) {
            Toast.makeText(this, "Password must have more than 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Authenticates user with email and password.
        authenticateNewUser(email, password);

    }

    //Creates an user authentication in Firebase.
    private void authenticateNewUser(final String email, final String password) {
        auth = FirebaseAuth.getInstance();
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
        String userId = mDatabase.push().getKey();
        //creating user object.
        User user = new User(UUID, email, newUsername.getText().toString(), password, newName.getText().toString(),
                "example_birth", cbIsAdmin.isChecked());

        //pushing user to 'users' node using the userId.
        mDatabase.child(userId).setValue(user);
        Toast.makeText(getApplicationContext(), "Signup successfull!!", Toast.LENGTH_SHORT).show();


        progressDialog.dismiss();
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }


}
