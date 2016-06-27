package com.gmail.abanoubwagih.gp_project.Log_in_auth_and_Synchronous_Data;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.BuildingListActivity;
import com.gmail.abanoubwagih.gp_project.BuildingHandle.DataProvidingFromFirebase;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SynchronizeData extends Service {

    public static String userName = null;
    private static long UPDATE_INTERVAL = 1 * 5 * 1000;  //default
    private static long count = 1;  //default

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

    public void retriveData() {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
        userName = sharedPreferences.getString(getString(R.string.loginName), "abanoubwagih");
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
                            if (BuildingListActivity.buildings != null) {
                                if (!BuildingListActivity.buildings.isEmpty()) {

                                    BuildingListActivity.buildings.clear();
                                    BuildingListActivity.buildings.addAll(user.getBuilding());
                                } else {
                                    BuildingListActivity.buildings.addAll(user.getBuilding());

                                }
                            } else {
                                BuildingListActivity.buildings = new ArrayList<>();
                                BuildingListActivity.buildings.addAll(user.getBuilding());
                            }
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

        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
            userName = sharedPreferences.getString(getString(R.string.loginName), "abanoubwagih");
            if (userName != null) {
                if (mDatabase == null)
                    mDatabase = firebaseDatabase.getReference();

//        if(userName) userName = "abanoubwagih";
                if (mUserReference == null) {
                    mUserReference = mDatabase.child("users").child(userName);
                    mUserReference.keepSynced(true);
                }


                mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            DataProvidingFromFirebase.clearBuildingListandMap();
                            DataProvidingFromFirebase.addBuilding(user.getBuilding());
                            if (BuildingListActivity.buildings != null) {
                                if (!BuildingListActivity.buildings.isEmpty()) {

                                    BuildingListActivity.buildings.clear();
                                    BuildingListActivity.buildings.addAll(user.getBuilding());
                                } else {
                                    BuildingListActivity.buildings.addAll(user.getBuilding());

                                }
                            } else {
                                BuildingListActivity.buildings = new ArrayList<>();
                                BuildingListActivity.buildings.addAll(user.getBuilding());
                            }

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
                        Log.e("FailedToLoadSynch", databaseError.getMessage());
                        FirebaseCrash.report(databaseError.toException());
                    }
                });
            }
        } catch (Exception e) {
        }

//
//        try {
//            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
//            String name = sharedPreferences.getString(getString(R.string.loginName), "abanoubwagih");
//            String usedName2 = name != "abanoubwagih" ?
//                    (name.split("@")[0].contains(".") ? name.split("@")[0].split(".")[0] : name.split("@")[0])
//                    : "abanoubwagih";
//            if (usedName2 != null){
//
//                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//                mDatabase.child("users").child(usedName2).child("city").setValue("Cairo"+String.valueOf(count));
//                count ++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

        timer.scheduleAtFixedRate(

                new TimerTask() {

                    public void run() {

                        doServiceWork();

                    }
                }, 10000, UPDATE_INTERVAL);

        Log.i("LocalService", "name" + userName + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
}
