package com.gmail.abanoubwagih.gp_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.gmail.abanoubwagih.gp_project.Log_in_auth.EmailPasswordActivity;
import com.gmail.abanoubwagih.gp_project.UserHandler.RegistrationToken;

public class LaunchActivty extends AppCompatActivity {

    public static Context context;
    public RegistrationToken token;
    private CountDown countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        context = getApplicationContext();
//        register for token id for connection
        token = new RegistrationToken();
        token.onTokenRefresh();
////        Upload.uploadToFirebase();//uplaod data to firebase
//        Toast.makeText(LaunchActivty.this, "data upload", Toast.LENGTH_LONG).show();


        countDown = new CountDown(4000, 1000);
        countDown.start();

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
            startActivity(new Intent(LaunchActivty.this, EmailPasswordActivity.class));
        }
    }
}
