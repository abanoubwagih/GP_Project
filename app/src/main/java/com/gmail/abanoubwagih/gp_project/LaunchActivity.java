package com.gmail.abanoubwagih.gp_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.Firebase;
import com.gmail.abanoubwagih.gp_project.Log_in_auth_and_Synchronous_Data.EmailPasswordActivity;
import com.gmail.abanoubwagih.gp_project.UserHandler.RegistrationToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.crash.FirebaseCrash;

public class LaunchActivity extends AppCompatActivity {

    private static final int requestPermission = 578;
    private static final int SERVICE_RERROR_REQUEST_CODE = 9001;
    public static Context context;
    public RegistrationToken token;
    private CountDown countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        try {
            context = getApplicationContext();

//            new DeleteImagesTask().execute();

            if (!isGooglePlayServicesAvailable()) {
                finish();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    token = new RegistrationToken();
                    token.onTokenRefresh();
                    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.pref_general, false);
                    PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.pref_notification, false);
                }
            }).start();
//        Upload.uploadToFirebase();//uplaod data to firebase
//        Toast.makeText(LaunchActivity.this, "data upload", Toast.LENGTH_LONG).show();
            if (!isPermissionAvailable()) {
                finish();
            } else {
                startWork();
            }


        } catch (Exception e) {
            Log.d(getString(R.string.Tag_launchActivity), e.getMessage());
            FirebaseCrash.report(e);
        }

    }

    public void startWork() {
        countDown = new CountDown(4000, 1000);
        countDown.start();
    }

    public void endWork() {
        finish();
    }

    public boolean isPermissionAvailable() {

        if (Build.VERSION.SDK_INT > 22) {
            if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {

                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.INTERNET)
                        || !shouldShowRequestPermissionRationale(android.Manifest.permission.RECEIVE_SMS)
                        || !shouldShowRequestPermissionRationale(android.Manifest.permission.CHANGE_WIFI_STATE)
                        || !shouldShowRequestPermissionRationale(android.Manifest.permission.READ_SMS)) {

                    requestPermissions(
                            new String[]{android.Manifest.permission.READ_SMS
                                    , android.Manifest.permission.RECEIVE_SMS,
                                    android.Manifest.permission.INTERNET
                                    , android.Manifest.permission.CHANGE_WIFI_STATE}
                            , requestPermission);

                    return true;
                }

                return false;
            }
        }
        return true;
    }

    private boolean isGooglePlayServicesAvailable() {

        try {
            int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

            if (available == ConnectionResult.SUCCESS) {
                Log.d("GooglePlayServices", "available");
                return true;
            } else if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
                Log.d("GooglePlayServices", "not available and user can handle it by dialog ");
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(available, LaunchActivity.this, SERVICE_RERROR_REQUEST_CODE);
                dialog.show();
            } else {
                Log.d("GooglePlayServices", "not available can't run it");

            }
            return false;
        } catch (Exception e) {
            Log.d("GooglePlayServices", "error happened :" + e.getMessage());

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == requestCode) {
            if (
                    permissions[0] == android.Manifest.permission.RECEIVE_SMS &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {

                finish();
            }
        }
    }

    public class CountDown extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            startActivity(new Intent(LaunchActivity.this, EmailPasswordActivity.class));
        }
    }

    private class DeleteImagesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.this);

            boolean isFirstRun = wmbPreference.getBoolean(getString(R.string.FIRSTRUN), true);
            if (isFirstRun) {
                // Code to run once
                token = new RegistrationToken();
                token.onTokenRefresh();
                //set application to firebase integration it already done by json file in app
                Firebase.setAndroidContext(LaunchActivity.this);
                //enable offline persistent
                Firebase.getDefaultConfig().setPersistenceEnabled(true);
                //        register for token id for connection
                SharedPreferences.Editor editor = wmbPreference.edit();

                editor.putBoolean(getString(R.string.FIRSTRUN), false);
                editor.commit();
                editor.apply();
            }
            return null;
        }

    }
}
