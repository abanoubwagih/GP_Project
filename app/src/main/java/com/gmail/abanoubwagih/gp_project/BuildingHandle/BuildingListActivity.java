package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.gmail.abanoubwagih.gp_project.setting.SettingsActivity;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class BuildingListActivity extends AppCompatActivity {


    public static final String BUILDING_ID = "BUILDING_ID";
    public static List<Building> buildings;
    public static String userName = null;
    public static BuildingListAdapter adapter;
    public static ListView lv;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference mUserReference;
    public DatabaseReference mDatabase;
    int requestCode = 1441;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_building_list);
//        startService(new Intent(getApplication(), SynchronizeData.class));

        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
            String name = sharedPreferences.getString(getString(R.string.loginName), "abanoubwagih");
            userName = name != "abanoubwagih" ?
                    (name.split("@")[0].contains(".") ? name.split("@")[0].split(".")[0] : name.split("@")[0])
                    : "abanoubwagih";

            if (userName != null) {
                if (mDatabase == null) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    mDatabase = firebaseDatabase.getReference();
                }
//        if(userName) userName = "abanoubwagih";
                if (mUserReference == null) {
                    mUserReference = mDatabase.child("users").child(userName);
                    mUserReference.keepSynced(true);
                }


                mUserReference.addValueEventListener(new ValueEventListener() {
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
                            adapter.notifyDataSetChanged();
                            lv.invalidateViews();
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            prefsEditor.putString(getString(R.string.usrObject), json);
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
            FirebaseCrash.report(e);
            Log.d(getString(R.string.Tag_listActivity), e.getMessage());

        }

        try {


            buildings = DataProvidingFromFirebase.getBuildingList();
            if (buildings == null || buildings.isEmpty()) {

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
                if (user != null) {
                    DataProvidingFromFirebase.clearBuildingListandMap();
                    DataProvidingFromFirebase.addBuilding(user.getBuilding());
                    if (BuildingListActivity.buildings != null || !BuildingListActivity.buildings.isEmpty())
                        buildings.clear();
                    buildings.addAll(user.getBuilding());
                }
            }
            adapter = new BuildingListAdapter(
                    this, R.layout.list_item, buildings);
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        Intent intent = new Intent(BuildingListActivity.this, BuildingDetailsActivty.class);

                        Building building = buildings.get(position);

                        intent.putExtra(BUILDING_ID, building.getBuildingId());

                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("not work", e.getMessage());
                    }
                }
            });

            lv.setOnHoverListener(new View.OnHoverListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onHover(View v, MotionEvent event) {
                    v.setBackground(getDrawable(R.drawable.item_list_background_hover));
                    return true;
                }
            });

            adapter.notifyDataSetChanged();
            lv.invalidateViews();
            adapter.setNotifyOnChange(true);
        } catch (JsonSyntaxException e) {
            Log.d(getString(R.string.Tag_listActivity), e.getMessage());
            FirebaseCrash.report(e);
        }
    }

    @Override
    protected void onResume() {

        try {
            super.onResume();
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                String name = sharedPreferences.getString(getString(R.string.loginName), "abanoubwagih");
                userName = name != "abanoubwagih" ?
                        (name.split("@")[0].contains(".") ? name.split("@")[0].split(".")[0] : name.split("@")[0])
                        : "abanoubwagih";

                if (userName != null) {
                    if (mDatabase == null) {
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        mDatabase = firebaseDatabase.getReference();
                    }
//        if(userName) userName = "abanoubwagih";
                    if (mUserReference == null) {
                        mUserReference = mDatabase.child("users").child(userName);
                        mUserReference.keepSynced(true);
                    }


                    mUserReference.addValueEventListener(new ValueEventListener() {
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
                                adapter.notifyDataSetChanged();
                                lv.invalidateViews();
                                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(user);
                                prefsEditor.putString(getString(R.string.usrObject), json);
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
                FirebaseCrash.report(e);
                Log.d(getString(R.string.Tag_listActivity), e.getMessage());

            }

            if (buildings == null || buildings.isEmpty()) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
                DataProvidingFromFirebase.clearBuildingListandMap();
                DataProvidingFromFirebase.addBuilding(user.getBuilding());
                if (BuildingListActivity.buildings != null || !BuildingListActivity.buildings.isEmpty())
                    buildings.clear();
                buildings.addAll(user.getBuilding());
            }
            adapter.notifyDataSetChanged();
            lv.invalidateViews();
        } catch (JsonSyntaxException e) {
            Log.d(getString(R.string.Tag_listActivity), e.getMessage());
            FirebaseCrash.report(e);
        }

    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                String name = sharedPreferences.getString(getString(R.string.loginName), "abanoubwagih");
                userName = name != "abanoubwagih" ?
                        (name.split("@")[0].contains(".") ? name.split("@")[0].split(".")[0] : name.split("@")[0])
                        : "abanoubwagih";

                if (userName != null) {
                    if (mDatabase == null) {
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        mDatabase = firebaseDatabase.getReference();
                    }
//        if(userName) userName = "abanoubwagih";
                    if (mUserReference == null) {
                        mUserReference = mDatabase.child("users").child(userName);
                        mUserReference.keepSynced(true);
                    }


                    mUserReference.addValueEventListener(new ValueEventListener() {
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
                                adapter.notifyDataSetChanged();
                                lv.invalidateViews();
                                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(user);
                                prefsEditor.putString(getString(R.string.usrObject), json);
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
                FirebaseCrash.report(e);
                Log.d(getString(R.string.Tag_listActivity), e.getMessage());

            }

            if (buildings == null || buildings.isEmpty()) {

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
                DataProvidingFromFirebase.clearBuildingListandMap();
                DataProvidingFromFirebase.addBuilding(user.getBuilding());
                if (BuildingListActivity.buildings != null || !BuildingListActivity.buildings.isEmpty())
                    buildings.clear();
                buildings.addAll(user.getBuilding());
            }
            adapter.notifyDataSetChanged();
            lv.invalidateViews();
        } catch (JsonSyntaxException e) {
            Log.d(getString(R.string.Tag_listActivity), e.getMessage());
            FirebaseCrash.report(e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // // TODO: 6/25/2016  on save state
//        onSaveInstanceState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.Menu_item_setting) {
            startActivityForResult(new Intent(this, SettingsActivity.class), requestCode);
        }
        return true;

    }
}
