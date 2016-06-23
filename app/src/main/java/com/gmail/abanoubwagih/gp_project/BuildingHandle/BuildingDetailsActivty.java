package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.abanoubwagih.gp_project.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.gmail.abanoubwagih.gp_project.BuildingHandle.BuildingListActivity.BUILDING_ID;

public class BuildingDetailsActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details_activty);
        try {
            int buildingID = getIntent().getIntExtra(BUILDING_ID, 0);


            Building building = DataProvidingFromFirebase.buildingMap.get(buildingID);

            TextView tv = (TextView) findViewById(R.id.buildingName);
            tv.setText(building.getName());

            TextView descView = (TextView) findViewById(R.id.descriptionText);
            descView.setText(building.getDescription());

            // // TODO: 6/20/2016 replace the true or false with color

//        String status = String.valueOf(building.isStatus());
            String status = building.isStatus() ? "save" : "FIre";
            TextView state = (TextView) findViewById(R.id.buildingStatus);
            state.setText(status);

            ImageView iv = (ImageView) findViewById(R.id.buildingImage);
            InputStream stream = new ByteArrayInputStream(Base64.decode(building.getPhoto().getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            iv.setImageBitmap(bitmap);

        } catch (Exception e) {
            Log.e("not work detail", e.getMessage());
        }
    }

}
