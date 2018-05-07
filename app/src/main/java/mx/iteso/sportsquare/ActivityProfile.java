package mx.iteso.sportsquare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class ActivityProfile extends AppCompatActivity {

    private TextView tvTitle;
    private Button btnEditInfo;
    private Button btnInviteFriend;
    private static final int REQUEST_INVITE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvTitle = findViewById(R.id.tv_userprofile);
        btnEditInfo = findViewById(R.id.btn_editInfo);
        btnInviteFriend = findViewById(R.id.btn_InviteFriend);

        onInviteFriend();
        getUserData();
        onEditInfo();
    }

    private void onEditInfo() {
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityEditInfo.class);
                startActivity(intent);
            }
        });

    }

    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();

            Log.d(TAG, "getUserData: " + name + "--" + email + "--" + uid);

            tvTitle.setText(email);
        }

    }

    private void onInviteFriend() {
        btnInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvitation("Usa la app!!");
            }
        });
    }

    private void sendInvitation(String message) {
        /*Intent intent = new AppInviteInvitation.IntentBuilder("SPORTSQUARE")
                .setMessage("Te invito a usar la app!!")
                .setDeepLink(Uri.parse("https://wpbw6.app.goo.gl/rniX"))
                .setCallToActionText("Find data")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);*/


        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setEmailSubject(getString(R.string.invitation_email_subject))
                .setDeepLink(Uri.parse(getString(R.string.app_link)))
                .setMessage(message)
                .setEmailHtmlContent(getString(R.string.invitation_email_html_content))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                Log.d(TAG, "onActivityResult: INVITATION FAILED!!!!!");
                // [END_EXCLUDE]
            }
        }
    }

}
