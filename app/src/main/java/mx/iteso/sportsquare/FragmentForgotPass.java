package mx.iteso.sportsquare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

/**
 * Dialog Fragment for users that forgot their password.
 * Created by dgalindo on 21/03/18.
 */

public class FragmentForgotPass extends DialogFragment {

    private EditText etEmailText;
    private Button btnPassSend, btnPassCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgotpass, container, false);
        etEmailText = v.findViewById(R.id.editText_fragEmail);
        btnPassCancel = v.findViewById(R.id.btn_passCancel);
        btnPassSend = v.findViewById(R.id.btn_passSend);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnPassSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmailText.getText().toString().equals("")) {
                    return;
                }

                String email = etEmailText.getText().toString();
                onForgotPass(email);

            }
        });

        btnPassCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void onForgotPass(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d("RECOVER_LOG", "SUCCESSFUL!!");
                dismiss();
            }
        });
    }

}
