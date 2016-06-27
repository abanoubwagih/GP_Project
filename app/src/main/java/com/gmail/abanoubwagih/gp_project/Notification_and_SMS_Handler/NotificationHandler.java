package com.gmail.abanoubwagih.gp_project.Notification_and_SMS_Handler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.BuildingDetailsActivty;
import com.gmail.abanoubwagih.gp_project.BuildingHandle.DataProvidingFromFirebase;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by AbanoubWagih on 6/19/2016.
 */
public class NotificationHandler extends FirebaseMessagingService {

    public static final String BUILDING_ID = "BUILDING_ID";
    private static final String TAG = "NotificationHandler";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Intent intentTOsmsActivity = new Intent(getApplicationContext(), BuildingDetailsActivty.class);

        intentTOsmsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            String body = remoteMessage.getNotification().getBody().split("@")[1];
            String num = body.trim();
            int id = Integer.parseInt(num);
            handleListOffline(id);
            intentTOsmsActivity.putExtra(BUILDING_ID, id);


            startActivity(intentTOsmsActivity);
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }

    public void handleListOffline(int id) {

        try {
            if (DataProvidingFromFirebase.getBuildingMap() != null && !DataProvidingFromFirebase.getBuildingMap().isEmpty())
                DataProvidingFromFirebase.getBuildingMap().get(id).setStatus(false);

            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.sharedPreferences), Context.MODE_PRIVATE);

            if (sharedPreferences != null) {
                Gson gson = new Gson();
                String json = sharedPreferences.getString(getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
                if (user.getBuilding() != null && !user.getBuilding().isEmpty()) {
                    user.getBuilding().get(id - 1).setStatus(false);

                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(user);
                    prefsEditor.putString(getString(R.string.usrObject), json);
//                    prefsEditor.commit();
                    prefsEditor.apply();
                }
            }
        } catch (JsonSyntaxException e) {
            FirebaseCrash.report(e);
        }

    }

}
