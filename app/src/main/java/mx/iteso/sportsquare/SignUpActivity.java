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


    private EditText newUsernameET;
    private EditText newPasswordET;
    private EditText retypePassET;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        newUsernameET = findViewById(R.id.etSignupUsername);
        newPasswordET = findViewById(R.id.etSignupPassword);
        retypePassET = findViewById(R.id.etRetypePass);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button signupBtn = findViewById(R.id.btnSignup);
        signupBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if (TextUtils.isEmpty(newUsernameET.getText().toString())
                || TextUtils.isEmpty(newPasswordET.getText().toString())
                || TextUtils.isEmpty(retypePassET.getText().toString())) {

            Toast.makeText(this, "You MUST fill every field to continue!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.equals(newPasswordET.getText().toString(), retypePassET.getText().toString())) {
            Toast.makeText(this, "Both password fields MUST match", Toast.LENGTH_SHORT).show();
            return;
        }


        String username = newUsernameET.getText().toString();
        String password = newPasswordET.getText().toString();
        Log.d("USER_TAG", "Username: " + username + " >> Pass: " + password);

        insertNewUserToDB();
    }


    private void insertNewUserToDB() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //Get Firebase auth instance
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //creates new userId for firebase.
        String userId = mDatabase.push().getKey();

        //creating user object.
        User user = new User("liu.diego@hotmail.com","prueba123", "contra123", "Diego",
                "Galindo", "14-02-92", true);

        //pushing user to 'users' node using the userId.
        mDatabase.child(userId).setValue(user);

        Toast.makeText(this, "Signup successfull!!", Toast.LENGTH_SHORT).show();


        //create user
        auth.createUserWithEmailAndPassword("liu.diego@email.com", "contra123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH_LOG", "Authentication successful");
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

}
