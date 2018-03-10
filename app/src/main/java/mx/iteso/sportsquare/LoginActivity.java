package mx.iteso.sportsquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText userTxtBx;
    private EditText paswordTxtBx;
    private TextView txt;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        userTxtBx = findViewById(R.id.userTxtBx);
        paswordTxtBx = findViewById(R.id.paswordTxtBx);

    }

    public void onClick(View v){
        password = Hash.sha1( paswordTxtBx.getText().toString());
        System.out.println(password);
    }
}
