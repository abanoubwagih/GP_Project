package com.gmail.abanoubwagih.gp_project.Log_in_auth_and_Synchronous_Data;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
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
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class EmailPasswordActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    public ProgressDialog mProgressDia;
    public ValueEventListener postListener;
    public DatabaseReference mUserReference;
    public RetrieveData retrieveData;
    private EditText mEmailField;
    private EditText mPasswordField;
    private SharedPreferences sharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean dataHasBeenStored;

    // [END declare_auth_listener]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        try {
            mEmailField = (EditText) findViewById(R.id.field_email);
            mPasswordField = (EditText) findViewById(R.id.field_password);
            firebaseDatabase = FirebaseDatabase.getInstance();
            mProgressDia = new ProgressDialog(EmailPasswordActivity.this);
//        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean isFirstRunbase = wmbPreference.getBoolean(getString(R.string.isFirstRunbase), true);
//        if (isFirstRunbase)
//        {
//            firebaseDatabase.setPersistenceEnabled(true);
//            SharedPreferences.Editor editor = wmbPreference.edit();
//            editor.putBoolean("FIRSTRUN", false);
//            editor.commit();
//        }

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
                    goToListAndRetriveDataAndUpdateUI(user);
                    // [END_EXCLUDE]
                }
            };
            // [END auth_state_listener]
        } catch (Exception e) {
            Log.d(getString(R.string.Tag_EmailPasswordActivity), e.getMessage());
            FirebaseCrash.report(e);
        }
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        try {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        } catch (Exception e) {
            Log.d(getString(R.string.Tag_EmailPasswordActivity), e.getMessage());
            FirebaseCrash.report(e);
        }

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        try {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        } catch (Exception e) {
            Log.d(getString(R.string.Tag_EmailPasswordActivity), e.getMessage());
            FirebaseCrash.report(e);
        }
//        mAuth.signOut();
    }
    // [END on_stop_remove_listener]


    private void signIn(String email, String password) {

        try {
            Log.d(TAG, "signIn:" + email);
            if (!validateForm()) {
                return;
            }
            sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.loginName), email);
            editor.commit();
            editor.apply();
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
        } catch (Exception e) {
            Log.d(getString(R.string.Tag_EmailPasswordActivity), e.getMessage());
            FirebaseCrash.report(e);
        }
    }


    private boolean validateForm() {
        boolean valid = true;

        try {
            String email = mEmailField.getText().toString();
            if (TextUtils.isEmpty(email) || !email.contains("@")) {
                mEmailField.setError("Required.");
                valid = false;
            } else {
                mEmailField.setError(null);
            }

            String password = mPasswordField.getText().toString();
            if (TextUtils.isEmpty(password) || password.length() < 4) {
                mPasswordField.setError("Required.");
                valid = false;
            } else {
                mPasswordField.setError(null);
            }
        } catch (Exception e) {
            Log.d(getString(R.string.Tag_EmailPasswordActivity), e.getMessage());
            FirebaseCrash.report(e);
        }

        return valid;
    }

    private void goToListAndRetriveDataAndUpdateUI(FirebaseUser user) {
        try {
            hideProgressDialog();
            if (user != null) {

                findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
                findViewById(R.id.email_password_fields).setVisibility(View.GONE);
                sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                dataHasBeenStored = sharedPreferences.getBoolean(getString(R.string.dataHasBeenStored), false);

                if (dataHasBeenStored) {


                    Intent intent = new Intent(EmailPasswordActivity.this, BuildingListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {


                    retrieveData = new RetrieveData();
                    sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                    String name = sharedPreferences.getString(getString(R.string.loginName), "abanoubwagih");

                    String usedName = name != "abanoubwagih" ?
                            (name.split("@")[0].contains(".") ? name.split("@")[0].split(".")[0] : name.split("@")[0])
                            : "abanoubwagih";
                    retrieveData.execute(usedName);

                }
            } else {

                if (!isNetworkConnected()) {

                    new AlertDialog.Builder(this).setMessage("your Internet connection \nis not available")
                            .setPositiveButton("open", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                                    wifiManager.setWifiEnabled(true);

                                    startActivityForResult(
                                            new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 997);
                                }
                            })
                            .setNegativeButton("close app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            }).create().show();
                }
                Toast.makeText(this, "please log in ", Toast.LENGTH_LONG).show();
                findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            Log.d(getString(R.string.Tag_EmailPasswordActivity), e.getMessage());
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.email_sign_in_button:
                    signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                    break;
                case R.id.email_forget_password_button:
                    startActivity(new Intent(EmailPasswordActivity.this, PasswordResetEmail.class));
                    Log.d("email_forget_password", "rest");
                    break;
            }
        } catch (Exception e) {
            Log.d(getString(R.string.Tag_EmailPasswordActivity), e.getMessage());
            FirebaseCrash.report(e);
        }
    }


    public boolean isNetworkConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else if (networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public class RetrieveData extends AsyncTask<String, String, String> {
        private static final String TAG = "RetrieveData";
        private DatabaseReference mDatabase;


        // show loading progress
        public void showProgressDia(int value) {


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
            SynchronizeData.setUserName(userName);
            mDatabase = firebaseDatabase.getReference();

//        if(userName) userName = "abanoubwagih";
            mUserReference = mDatabase.child("users").child(userName);
            mUserReference.keepSynced(true);
            postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get user object and use the values to update the UI
                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        DataProvidingFromFirebase.clearBuildingListandMap();
                        DataProvidingFromFirebase.addBuilding(user.getBuilding());


                        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        prefsEditor.putString(getString(R.string.usrObject), json);
                        prefsEditor.commit();
                        prefsEditor.apply();


                        dataHasBeenStored = true;
                        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                        SharedPreferences.Editor editordataHasBeenStored = sharedPreferences.edit();
                        editordataHasBeenStored.putBoolean(getString(R.string.dataHasBeenStored), dataHasBeenStored);
                        editordataHasBeenStored.commit();
                        editordataHasBeenStored.apply();

                    }
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
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

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
