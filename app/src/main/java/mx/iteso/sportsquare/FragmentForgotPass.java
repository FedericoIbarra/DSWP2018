package mx.iteso.sportsquare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Dialog Fragment for users that forgot their password.
 * Created by dgalindo on 21/03/18.
 */

public class FragmentForgotPass extends DialogFragment {

    FirebaseAuth auth;
    EditText etEmailText;
    ProgressBar progressBar;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        etEmailText = getActivity().findViewById(R.id.editText_fragEmail);
        progressBar = getActivity().findViewById(R.id.progressBar_frag);

        builder.setView(inflater.inflate(R.layout.fragment_forgotpass, null))
                .setPositiveButton(R.string.pass_recovery_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);

                        if (TextUtils.isEmpty(etEmailText.getText())) {
                            return;
                        }

                        String email = etEmailText.getText().toString();
                        onForgotPass(email);

                        progressBar.setVisibility(View.GONE);

                    }
                }).setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }



    private void onForgotPass(String email) {
        this.auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d("RECOVER_LOG", "SUCCESSFUL!!");
            }
        });
    }

}
