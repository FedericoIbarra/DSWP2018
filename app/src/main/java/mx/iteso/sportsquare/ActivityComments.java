package mx.iteso.sportsquare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.iteso.sportsquare.adapter.CommentAdapter;
import mx.iteso.sportsquare.beans.Comment;

public class ActivityComments extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView publication;
    private EditText commentText;
    private ImageView commentBtn;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    private String idPublication;
    private String publicationText;
    private ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        idPublication = getIntent().getStringExtra("idPublication");
        publicationText = getIntent().getStringExtra("publicationText");

        publication = findViewById(R.id.activity_comments_publication);
        commentText = findViewById(R.id.activity_comments_comment_text);
        commentBtn = findViewById(R.id.activity_comments_comment);

        publication.setText(publicationText);
        comments = new ArrayList<>();

        mRecyclerView = findViewById(R.id.activity_comments_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CommentAdapter(this, comments);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        commentAction();
        getComments();

    }

    private void getComments() {
        mDatabase.child("publications").child(currentUser.getUid()).child(idPublication).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Comment comment = data.getValue(Comment.class);
                    comments.add(comment);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void commentAction() {
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commentText.getText().toString().equals("")){
                    String id = mDatabase.child("publications").child(currentUser.getUid()).child("comments").push().getKey();
                    Comment comment = new Comment(idPublication, id, commentText.getText().toString());
                    mDatabase.child("publications").child(currentUser.getUid()).child(idPublication).child("comments").child(id).setValue(comment);
                    commentText.setText("");
                }
            }
        });
    }
}
