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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ContactForm extends AppCompatActivity implements View.OnClickListener{

    private EditText emailET;
    private EditText phoneET;
    private EditText socialET;
    private Button button;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        emailET = findViewById(R.id.etEstbContactEmail);
        phoneET = findViewById(R.id.etEstbPhone);
        socialET = findViewById(R.id.etEstbSocialNetworks);
        button = findViewById(R.id.btnEstbContact);

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        String email = bundle.getString("email");
        String establishmentName = bundle.getString("establishmentName");
        String password = bundle.getString("password");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //TODO implement CRUD firebase
        Intent intent = new Intent(view.getContext(), ActivityMain.class);
        startActivity(intent);
    }
}

