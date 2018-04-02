package mx.iteso.sportsquare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class EstablishmentSignUp extends SignUpActivity {

    private EditText emailET;
    private EditText passwordET;
    private EditText retypePassET;
    private EditText usernameET;
    private EditText establishmentNameET;
    private Button button;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment_sign_up);

        emailET = findViewById(R.id.etEstbEmail);
        usernameET = findViewById(R.id.etEstbUsername);
        establishmentNameET = findViewById(R.id.etEstbName);
        passwordET = findViewById(R.id.etEstbPassword);
        retypePassET = findViewById(R.id.etEstbRepassword);
        button = findViewById(R.id.btnEstbSignup);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        //Check if the EditTexts are empty. If true: show toast.
        if (TextUtils.isEmpty(emailET.getText().toString())
                || TextUtils.isEmpty(usernameET.getText().toString())
                || TextUtils.isEmpty(establishmentNameET.getText().toString())
                || TextUtils.isEmpty(passwordET.getText().toString())
                || TextUtils.isEmpty(retypePassET.getText().toString())
                ) {
            Toast.makeText(this, "You MUST fill every field to continue!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Confirms if the EditTexts of the passwords are correct. If false: show toast.
        if (!TextUtils.equals(passwordET.getText().toString(), retypePassET.getText().toString())) {
            Toast.makeText(this, "Both password fields MUST match", Toast.LENGTH_SHORT).show();
            return;
        }


        String email = emailET.getText().toString();
        String password = Hash.sha1(passwordET.getText().toString());

        //Check if email has @.
        if (!isEmailValid(email)) {
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if password have more than 6 chars (Firebase rule).
        if (passwordET.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must have more than 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Authenticates user with email and password.
        //authenticateNewUser(email, password);

        Intent intent = new Intent(view.getContext(), ContactForm.class);
        intent.putExtra("username", usernameET.getText().toString());
        intent.putExtra("email", emailET.getText().toString());
        intent.putExtra("establishmentName", establishmentNameET.getText().toString());
        intent.putExtra("password", passwordET.getText().toString());
        startActivity(intent);
    }

}

