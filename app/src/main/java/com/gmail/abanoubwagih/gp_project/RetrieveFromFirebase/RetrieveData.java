package com.gmail.abanoubwagih.gp_project.RetrieveFromFirebase;

import android.util.Log;

import com.gmail.abanoubwagih.gp_project.BuildingHandle.DataProvidingFromFirebase;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by RINA on 6/22/2016.
 */
public class RetrieveData {
    private static final String TAG = "RetrieveData";
    private DatabaseReference mDatabase;

    public void refreshData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String userName = "abanoubwagih";
        DatabaseReference mUserReference = mDatabase.child("users").child(userName);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user object and use the values to update the UI

                User user = dataSnapshot.getValue(User.class);
                DataProvidingFromFirebase.addBuilding(user.getBuilding());
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mUserReference.addValueEventListener(postListener);


    }
}
