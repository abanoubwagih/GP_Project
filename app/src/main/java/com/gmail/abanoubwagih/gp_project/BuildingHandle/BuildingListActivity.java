package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.abanoubwagih.gp_project.Log_in_auth_and_Synchronous_Data.SynchronizeData;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

public class BuildingListActivity extends AppCompatActivity {


    public static final String BUILDING_ID = "BUILDING_ID";
    private List<Building> buildings;
    private BuildingListAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_building_list);
        try {

            startService(new Intent(getBaseContext(), SynchronizeData.class));

            buildings = DataProvidingFromFirebase.getBuildingList();
            if (buildings == null || buildings.isEmpty()) {

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
                if (user != null)
                    buildings.addAll(user.getBuilding());
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
            if (buildings == null || buildings.isEmpty()) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
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
            if (buildings == null || buildings.isEmpty()) {

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(getString(R.string.usrObject), "");
                User user = gson.fromJson(json, User.class);
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

}
