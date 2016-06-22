package com.gmail.abanoubwagih.gp_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.RetriveDataFromFirebase;
import com.gmail.abanoubwagih.gp_project.Log_in_auth.EmailPasswordActivity;
import com.gmail.abanoubwagih.gp_project.Notification_and_SMS_Handler.RegistrationToken;

public class LaunchActivty extends AppCompatActivity {

    private CountDown countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        RegistrationToken token = new RegistrationToken();
        token.onTokenRefresh();
        new RetriveDataFromFirebase().retrive();
        countDown = new CountDown(5000, 1000);
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
