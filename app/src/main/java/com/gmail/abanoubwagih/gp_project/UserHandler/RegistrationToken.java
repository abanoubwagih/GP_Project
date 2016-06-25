package com.gmail.abanoubwagih.gp_project.UserHandler;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by AbanoubWagih on 6/19/2016.
 */
public class RegistrationToken extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("RegistrationToken", "Refreshed token: " + refreshedToken);


    }

}
