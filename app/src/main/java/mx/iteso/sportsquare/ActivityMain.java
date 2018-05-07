package mx.iteso.sportsquare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.iteso.sportsquare.beans.User;


public class ActivityMain extends ActivityBase {
    private DatabaseReference mDatabase;
    private FirebaseUser currentFirebaseUser;
    private User userInfo;

    private FirebaseAuth auth;
    private TextView message;
    private Button button;
    private Button button2;

    private Button btn_login;
    private Button btn_logout;
    private Button btn_user_wall;
    private Button btn_delete_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        btn_login = findViewById(R.id.btn_loginActivity);
        btn_logout = findViewById(R.id.btn_logout);
        btn_user_wall = findViewById(R.id.activity_main_user_wall);
        btn_delete_user = findViewById(R.id.activity_main_delete_user);

        readUser();
        onBtnToLoginActivityClicked();
        onLogout();
        userWall();
        deleteUser();
    }

    private void readUser() {
        if (currentFirebaseUser == null) {
            return;
        }

        mDatabase.child("users").child(currentFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteUser() {
        btn_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(view.getContext());
                deleteDialogBuilder.setTitle("Delete user")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.child("users").child(currentFirebaseUser.getUid()).removeValue();
                                mDatabase.child("usernames").child(userInfo.username).removeValue();
                                mDatabase.child("publications").child(currentFirebaseUser.getUid()).removeValue();
                                currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                currentFirebaseUser.delete();

                                finish();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog deleteAlert =  deleteDialogBuilder.create();
                deleteAlert.show();
            }
        });
    }

    private void userWall() {
        btn_user_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityUserWall.class);
                startActivity(intent);
            }
        });
    }

    //Check if the user is logged in, if not it will open the LoginActivity.
    //And check if the user have verify his account.
    private void checkUser(FirebaseUser currentUser, GoogleSignInAccount account) {

        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if (account == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        /*else if (!currentUser.isEmailVerified()) {
            Toast.makeText(this, "You Must verify your email!", Toast.LENGTH_LONG).show();
        }*/

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
                auth.signOut();
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        checkUser(currentUser, account);
    }
    
}
