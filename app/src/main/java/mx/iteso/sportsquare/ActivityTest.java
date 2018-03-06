package mx.iteso.sportsquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityTest extends AppCompatActivity {
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        back = findViewById(R.id.activity_test_back);

    }

    public void onClick(View v){
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }
}
