package com.gmail.abanoubwagih.gp_project.Log_in_auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.BuildingListActivity;
import com.gmail.abanoubwagih.gp_project.BuildingHandle.DataProvidingFromFirebase;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailPasswordActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_forget_password_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
//        mAuth.signOut();
    }
    // [END on_stop_remove_listener]


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog(R.string.login);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.\n be sure of email or password",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            RetrieveData2 retrieveData = new RetrieveData2();
            retrieveData.execute(mEmailField.getText().toString().split("@")[0]);


        } else {

            Toast.makeText(this, "please log in ", Toast.LENGTH_LONG).show();
            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.email_forget_password_button:
                startActivity(new Intent(EmailPasswordActivity.this, PasswordResetEmail.class));
                Log.d("wwwww","rest");
                break;
        }
    }

    public class RetrieveData2 extends AsyncTask<String, String, String> {
        private static final String TAG = "RetrieveData";
        private DatabaseReference mDatabase;
        private ProgressDialog mProgressDia;

        // show loading progress
        public void showProgressDia(int value) {

            mProgressDia = new ProgressDialog(EmailPasswordActivity.this);
            mProgressDia.setMessage(getString(value));
            mProgressDia.setIndeterminate(true);


            mProgressDia.show();
        }

        //hide loading progress
        public void hideProgressDia() {
            if (mProgressDia != null && mProgressDia.isShowing()) {

                mProgressDia.dismiss();
            }
        }

        public void refreshData(String userName) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

//        if(userName) userName = "abanoubwagih";
            DatabaseReference mUserReference = mDatabase.child("users").child(userName);

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get user object and use the values to update the UI

                    User user = dataSnapshot.getValue(User.class);
                    DataProvidingFromFirebase.addBuilding(user.getBuilding());
                    Log.d("download", "done");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDia();
                            Intent intent = new Intent(EmailPasswordActivity.this, BuildingListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            mUserReference.addValueEventListener(postListener);


        }


        @Override
        protected String doInBackground(String... params) {
            refreshData(params[0]);
            return "";
        }

        @Override
        protected void onPreExecute() {
            showProgressDia(R.string.loadingData);
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }
}
