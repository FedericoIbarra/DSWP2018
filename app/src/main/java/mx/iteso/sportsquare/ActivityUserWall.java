package mx.iteso.sportsquare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ActivityUserWall extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText publicationText;
    private ImageView publish;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    private ArrayList<Publication> publications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        publicationText = findViewById(R.id.activity_user_wall_publication_text);
        publish = findViewById(R.id.activity_user_wall_publish);

        publications = new ArrayList<>();

        mRecyclerView = findViewById(R.id.activity_user_wall_publications_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PublicationAdapter(this, publications);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        publishAction();
        getPublications();
    }

    private void getPublications() {
        mDatabase.child("publications").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publications.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Publication publication = data.getValue(Publication.class);
                    publications.add(publication);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void publishAction() {
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!publicationText.getText().toString().equals("")) {
                    String id = mDatabase.child("publications").child(currentUser.getUid()).push().getKey();
                    Publication publication = new Publication(id, publicationText.getText().toString());
                    mDatabase.child("publications").child(currentUser.getUid()).child(id).setValue(publication);
                    publicationText.setText("");
                }

            }
        });
    }
}
