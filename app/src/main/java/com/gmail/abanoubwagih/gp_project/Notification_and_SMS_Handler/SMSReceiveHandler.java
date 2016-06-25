package com.gmail.abanoubwagih.gp_project.Notification_and_SMS_Handler;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.BuildingDetailsActivty;
import com.gmail.abanoubwagih.gp_project.BuildingHandle.DataProvidingFromFirebase;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created by AbanoubWagih on 6/17/2016.
 */
public class SMSReceiveHandler extends BroadcastReceiver {
    private Context myContext;
    public SMSReceiveHandler() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle recievedIntentExtras = intent.getExtras();
        SmsMessage[] smsMessagesArray = null;
        myContext = context;
        String messages = "";

        // if message available, go on
        if (recievedIntentExtras != null) {
            // get the array of the message
            Object[] smsExtraOjects = (Object[]) recievedIntentExtras.get("pdus");
            smsMessagesArray = new SmsMessage[smsExtraOjects.length];
            // loop through the number of available messages
            for (int i = 0; i < smsExtraOjects.length; ++i) {
                // create smsmessage from raw pdu

                smsMessagesArray[i] = SmsMessage.createFromPdu((byte[]) smsExtraOjects[i]);

                // retrieve contents of message
                String body = smsMessagesArray[i].getMessageBody().toString();
                String address = smsMessagesArray[i].getOriginatingAddress().toString();

                Log.d("SMS_FromFirebase", address + "\nBody\n " + body);

//                only accept messages from specified number
                if (address.equalsIgnoreCase(context.getString(R.string.Firebase_Number))) {

                    //
                    Intent intentTOsmsActivity = new Intent(context.getApplicationContext(), BuildingDetailsActivty.class);

                    intentTOsmsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Bundle bundle = new Bundle();
//
//                    bundle.putSerializable(context.getString(R.string.sms_bodyToAnotherActivity),prepareSMS(body));
//                    intentTOsmsActivity.putExtras(bundle);

                    try {
                        String num = body.trim();
                        int id = Integer.parseInt(num);
                        handleListOffline(id);
                        intentTOsmsActivity.putExtra("BUILDING_ID", id);


                        context.startActivity(intentTOsmsActivity);
                    } catch (Exception e) {
                        FirebaseCrash.report(e);
                    }

                    // stop message from getting to default app
                    this.abortBroadcast();
                }
            }

        }
    }

    public HashMap<String, String> prepareSMS(String body) {

        HashMap<String, String> message = new HashMap<>();
        String[] mes = body.split("@");

        try {
            String num = mes[0].split("--")[1].trim();
            int id = Integer.parseInt(num);
            handleListOffline(id);
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }

        message.put(myContext.getString(R.string.sms_buildingname), mes[1].split("--")[1]);
        message.put(myContext.getString(R.string.sms_buildindStatus), mes[2].split("--")[1]);
        message.put(myContext.getString(R.string.sms_buildingDescription), mes[3].split("--")[1]);
        return message;
    }

    public void handleListOffline(int id) {

        try {
            if (DataProvidingFromFirebase.getBuildingMap() != null && !DataProvidingFromFirebase.getBuildingMap().isEmpty())
                DataProvidingFromFirebase.getBuildingMap().get(id).setStatus(false);

            SharedPreferences sharedPreferences = myContext.getSharedPreferences(
                    myContext.getString(R.string.sharedPreferences), Context.MODE_PRIVATE);

            if (sharedPreferences != null) {
                Gson gson = new Gson();
                String json = sharedPreferences.getString(myContext.getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
                if (user.getBuilding() != null && !user.getBuilding().isEmpty()) {
                    user.getBuilding().get(id - 1).setStatus(false);

                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(user);
                    prefsEditor.putString(myContext.getString(R.string.usrObject), json);
                    prefsEditor.commit();
                    prefsEditor.apply();
                }
            }
        } catch (JsonSyntaxException e) {
            FirebaseCrash.report(e);
        }

    }
}
