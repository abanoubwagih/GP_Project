package com.gmail.abanoubwagih.gp_project.Notification_and_SMS_Handler;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.BuildingListActivity;

/**
 * Created by AbanoubWagih on 6/17/2016.
 */
public class SMSReceiveHandler extends BroadcastReceiver {
    public SMSReceiveHandler() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        SmsMessage[] msgs = null;

        String messages = "";

        // if message available, go on
        if (extras != null) {
            // get the array of the message
            Object[] smsExtra = (Object[]) extras.get("pdus");
            msgs = new SmsMessage[smsExtra.length];
            // loop through the number of available messages
            for (int i = 0; i < smsExtra.length; ++i) {
                // create smsmessage from raw pdu

                msgs[i] = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

                // retrieve contents of message
                String body = msgs[i].getMessageBody().toString();
                String address = msgs[i].getOriginatingAddress().toString();
                Log.d("wwwwwww", address + " " + body);
//                 only accept messages from specified number
                if (address.equalsIgnoreCase("011485")) {

                    Intent intent1 = new Intent(context.getApplicationContext(), BuildingListActivity.class);
//                    intent1.setClassName("com.gmail.abanoubwagih.gp_project","com.gmail.abanoubwagih.gp_project.Notification_and_SMS_Handler.SMSReceiveHandler");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
//                    context.startService(intent);

                    // stop message from getting to default app
//                    this.abortBroadcast();
                }
            }

        }
    }
}
