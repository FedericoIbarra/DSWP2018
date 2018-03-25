package mx.iteso.sportsquare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Login Activity
 * Created by dgalindo on 10/03/18.
 */

public class LoginActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    TextView tv_forgotPass;
    TextView tv_signup;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.etEmail);
        et_password = findViewById(R.id.etPassword);
        btn_login = findViewById(R.id.btnLogin);
        tv_forgotPass = findViewById(R.id.tvForgotPassword);
        tv_signup = findViewById(R.id.tvSignup);
        onLoginAction();
        onSignupUser();


    }

    //Implementation of click for log in the user.
    private void onLoginAction() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                if (TextUtils.isEmpty(et_email.getText().toString())
                        || TextUtils.isEmpty(et_password.getText().toString())) {

                    Toast.makeText(view.getContext(), "You MUST fill every field to continue!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                String email = et_email.getText().toString();
                final String password = Hash.sha1(et_password.getText().toString());

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Failed to Login!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, ActivityMain.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    //TODO: Implements Firebase method for recovery password.
    private void onForgotPass() {

    }

    //Implementation of click for sign up a new user.
    private void onSignupUser() {
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


}
