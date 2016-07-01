package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.abanoubwagih.gp_project.Log_in_auth_and_Synchronous_Data.SynchronizeData;
import com.gmail.abanoubwagih.gp_project.MapAndRouteHandler.MapsActivity;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.setting.SettingsActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.gmail.abanoubwagih.gp_project.BuildingHandle.BuildingListActivity.BUILDING_ID;

public class BuildingDetailsActivty extends AppCompatActivity {

    int requestCode = 4141;
    int buildingIDForMap;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details_activty);
        try {
            int buildingID = getIntent().getIntExtra(BUILDING_ID, 0);

            buildingIDForMap = buildingID;
            Building building = DataProvidingFromFirebase.getBuildingMap().get(buildingID);
            if (building == null) {
                new SynchronizeData().retriveData();

                while (building == null) {
                    building = DataProvidingFromFirebase.getBuildingMap().get(buildingID);
                    Thread.sleep(2000);
                }
            }

            TextView tv = (TextView) findViewById(R.id.buildingName);
            tv.setText(building.getName());


//        String status = String.valueOf(building.isStatus());
            String status = building.isStatus() ? getString(R.string.buildingSafe) : getString(R.string.buildingFire);

            TextView state = (TextView) findViewById(R.id.buildingStatus);
            state.setText(status);

            TextView descView = (TextView) findViewById(R.id.descriptionText);
            descView.setText(building.getDescription());

            View view = findViewById(R.id.activity_details);
            if (building.isStatus()) {
                view.setBackground(getDrawable(R.drawable.item_list_background_safe));
                tv.setTextColor(Color.BLUE);
                state.setTextColor(Color.BLUE);

            } else {
                view.setBackground(getDrawable(R.drawable.item_list_background_fire));
                tv.setTextColor(Color.WHITE);
                state.setTextColor(Color.WHITE);
                descView.setTextColor(Color.WHITE);
//                descView.setBackground(getDrawable();

            }

//            final HashMap<String, Double> myGPS = building.getBuildingGPS();

            ImageView iv = (ImageView) findViewById(R.id.buildingImage);
            InputStream stream = new ByteArrayInputStream(Base64.decode(building.getPhoto().getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            iv.setImageBitmap(bitmap);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(BuildingDetailsActivty.this, MapsActivity.class);
                    intent.putExtra(BUILDING_ID, buildingIDForMap);
                    startActivity(intent);


                }
            });

        } catch (Exception e) {
            Log.e("not work detail", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_show_map) {
            Intent intent = new Intent(BuildingDetailsActivty.this, MapsActivity.class);
            intent.putExtra(BUILDING_ID, buildingIDForMap);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.Menu_item_setting) {
            startActivityForResult(new Intent(this, SettingsActivity.class), requestCode);
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentTOlistActivity = new Intent(this, BuildingListActivity.class);

        intentTOlistActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intentTOlistActivity);
        finish();
    }

}
