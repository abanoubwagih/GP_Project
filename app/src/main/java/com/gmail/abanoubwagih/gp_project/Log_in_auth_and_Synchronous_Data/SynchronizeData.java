package com.gmail.abanoubwagih.gp_project.Log_in_auth_and_Synchronous_Data;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.Building;
import com.gmail.abanoubwagih.gp_project.BuildingHandle.DataProvidingFromFirebase;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class SynchronizeData extends Service {

    public static String userName = null;
    private static long UPDATE_INTERVAL = 1 * 5 * 1000;  //default
    private static Timer timer = new Timer();
    public FirebaseDatabase firebaseDatabase;
    public ValueEventListener postListener;
    public DatabaseReference mUserReference;
    public DatabaseReference mDatabase;

    public SynchronizeData() {
    }

    public SynchronizeData(String userName) {
        SynchronizeData.userName = userName;
    }

    public static long getUpdateInterval() {
        return UPDATE_INTERVAL;
    }

    public static void setUpdateInterval(long updateInterval) {
        UPDATE_INTERVAL = updateInterval;
    }

    public static Timer getTimer() {
        return timer;
    }

    public static void setTimer(Timer timer) {
        SynchronizeData.timer = timer;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        SynchronizeData.userName = userName;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public void setFirebaseDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public ValueEventListener getPostListener() {
        return postListener;
    }

    public void setPostListener(ValueEventListener postListener) {
        this.postListener = postListener;
    }

    public DatabaseReference getmUserReference() {
        return mUserReference;
    }

    public void setmUserReference(DatabaseReference mUserReference) {
        this.mUserReference = mUserReference;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doServiceWork();
    }

    public void retriveData(){

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
        userName = sharedPreferences.getString(getString(R.string.loginName),"abanoubwagih");
        mDatabase.child("users").child(userName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            DataProvidingFromFirebase.clearBuildingListandMap();
                            DataProvidingFromFirebase.addBuilding(user.getBuilding());
//                            DataProvidingFromFirebase.getBuildingMap();
                        }
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("read once", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
    private void doServiceWork() {
        //do something wotever you want
        //like reading file or getting data from network
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
            userName = sharedPreferences.getString(getString(R.string.loginName),"abanoubwagih");
            if (userName != null) {
                if (mDatabase == null)
                    mDatabase = firebaseDatabase.getReference();

//        if(userName) userName = "abanoubwagih";
                if (mUserReference == null) {
                    mUserReference = mDatabase.child("users").child(userName);
                    mUserReference.keepSynced(true);
                }

                postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            DataProvidingFromFirebase.clearBuildingListandMap();
                            DataProvidingFromFirebase.addBuilding(user.getBuilding());


                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            prefsEditor.putString(getString(R.string.usrObject), json);
                            prefsEditor.commit();
                            prefsEditor.apply();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                mUserReference.addValueEventListener(postListener);
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doServiceWork();
        Log.i("LocalService", "name" + userName + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
}
