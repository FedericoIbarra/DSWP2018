package mx.iteso.sportsquare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.sportsquare.beans.User;

import static android.content.ContentValues.TAG;

public class ActivityEditInfo extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

    private DatabaseReference dbReference;
    private String uid;

    private TextView currentUsername;
    private TextView currentFullName;

    private EditText etNewUsername;
    private EditText etNewFullName;
    private CheckBox cbEmailNews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);

        currentUsername = findViewById(R.id.tv_theCurrentUSERNAME);
        currentFullName = findViewById(R.id.tv_theCurrentFULLNAME);
        etNewUsername = findViewById(R.id.et_currentUsername);
        etNewFullName = findViewById(R.id.et_currentFullName);
        cbEmailNews = findViewById(R.id.cb_currentNews);
        Button btnSaveChanges = findViewById(R.id.btn_currentSaveChanges);

        dbReference = FirebaseDatabase.getInstance().getReference();
        getAll();


        btnSaveChanges.setOnClickListener(this);
    }

    private void getAll() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            uid = user.getUid();

            DatabaseReference userDb = dbReference.getRef().child("users");
            userDb.addValueEventListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (!validateEditTexts()) {
            return;
        }

        updateToDB();

        Intent intent = new Intent(view.getContext(), ActivityProfile.class);
        startActivity(intent);
        finish();

    }


    private boolean validateEditTexts() {

        if (TextUtils.isEmpty(etNewUsername.getText())
                && TextUtils.isEmpty(etNewFullName.getText())) {
            return false;
        }

        return true;
    }

    private void updateToDB() {
        try {
            //dbReference.child(uid).child("awais@gmailcom").child("leftSpace").setValue("YourDateHere");

            dbReference.child("users").child(uid).child("fullName").setValue(etNewFullName.getText().toString());
            dbReference.child("users").child(uid).child("username").setValue(etNewUsername.getText().toString());
            dbReference.child("users").child(uid).child("emailNews").setValue(cbEmailNews.isChecked());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        boolean x = dataSnapshot.hasChild(uid);
        if (x) {
            User u = dataSnapshot.child(uid).getValue(User.class);

            if (u == null)
                return;

            currentUsername.setText(u.username);
            currentFullName.setText(u.fullName);
            cbEmailNews.setChecked(u.emailNews);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
