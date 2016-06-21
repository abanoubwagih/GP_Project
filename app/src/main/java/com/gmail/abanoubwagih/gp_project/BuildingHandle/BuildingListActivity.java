package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gmail.abanoubwagih.gp_project.R;

import java.util.List;

public class BuildingListActivity extends AppCompatActivity {


    public static final String BUILDING_ID = "BUILDING_ID";
    private List<Building> products = DataProvidingFromFirebase.buildingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        BuildingListAdapter adapter = new BuildingListAdapter(
                this, R.layout.list_item, products);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(BuildingListActivity.this, DetailActivity.class);
//
//                Building building = products.get(position);
//                intent.putExtra(BUILDING_ID, building.getBuildingId());
//
//                startActivity(intent);
            }
        });
    }
}
