package com.gmail.abanoubwagih.gp_project.Notification_and_SMS_Handler;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by AbanoubWagih on 6/19/2016.
 */
public class NotificationHandler extends FirebaseMessagingService {

    private static final String TAG = "NotificationHandler";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
