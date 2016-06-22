package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AbanoubWagih on 6/22/2016.
 */
public class RetriveDataFromFirebase {
    private DatabaseReference mDatabase;

    public void retrive() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        DatabaseReference mDatabaseReference = mDatabase.child("Customer").child("abanoubwagih").child("Building");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Building building = dataSnapshot.getValue(Building.class);
                Log.d("masr", building.toString());
                DataProvidingFromFirebase.addBuilding(building);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("retrive", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabaseReference.addValueEventListener(postListener);

    }
}
