package com.gmail.abanoubwagih.gp_project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.abanoubwagih.gp_project.Log_in_auth.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetEmail extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private EditText mPasswrodResetEmail;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_email);

        mPasswrodResetEmail = (EditText) findViewById(R.id.field_password_reset_email);

        // Buttons
        findViewById(R.id.email_forget_password_button_2).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_forget_password_button_2:

                String emailAddress = mPasswrodResetEmail.getText().toString();

                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(PasswordResetEmail.this, getString(R.string.mailSent),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                break;
        }
    }
}
