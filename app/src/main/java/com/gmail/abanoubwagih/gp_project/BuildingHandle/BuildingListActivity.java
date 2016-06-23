package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.abanoubwagih.gp_project.R;

import java.util.List;

public class BuildingListActivity extends AppCompatActivity {


    public static final String BUILDING_ID = "BUILDING_ID";
    private List<Building> buildings = DataProvidingFromFirebase.buildingList;
    private BuildingListAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_building_list);

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
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        lv.invalidateViews();
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        lv.invalidateViews();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
