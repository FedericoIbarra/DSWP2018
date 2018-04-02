package mx.iteso.sportsquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ActivityMain extends AppCompatActivity {

    private DatabaseReference mDatabase;

    
    FirebaseUser user;

    private FirebaseAuth auth;
    //private TextView message;
    private Button button;
    private Button button2;


    private TextView message;
    private Button btn_login;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        message = findViewById(R.id.activity_main_firebase_text);
        btn_login = findViewById(R.id.btn_loginActivity);
        btn_logout = findViewById(R.id.btn_logout);

        getMessageFromFirebase();
        checkUser();
        onBtnToLoginActivityClicked();
        onLogout();

    }

    //Simple Hello World from Firebase
    private void getMessageFromFirebase() {
        mDatabase.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                message.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityTest.class);
                startActivity(intent);
            }

        });
    }

    //Check if the user is logged in, if not it will open the LoginActivity.
    //And check if the user have verify his account.
    private void checkUser() {

        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if (!user.isEmailVerified()) {
            Toast.makeText(this, "You Must verify your email!", Toast.LENGTH_LONG).show();
        }

    }

    private void onBtnToLoginActivityClicked() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void onLogout() {
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        button2 = findViewById(R.id.button2establishment);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EstablishmentSignUp.class);
                startActivity(intent);
            }
        });


    }
}
