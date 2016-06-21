package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.abanoubwagih.gp_project.R;

import java.io.IOException;
import java.io.InputStream;

public class BuildingDetailsActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details_activty);

        String buildingID = getIntent().getStringExtra(BuildingListActivity.BUILDING_ID);
        Building building = DataProvidingFromFirebase.buildingMap.get(buildingID);

        TextView tv = (TextView) findViewById(R.id.buildingName);
        tv.setText(building.getName());

        TextView descView = (TextView) findViewById(R.id.descriptionText);
        descView.setText(building.getDescription());

        // // TODO: 6/20/2016 replace the true or false with color

        String status = String.valueOf(building.getStatus());
        TextView priceText = (TextView) findViewById(R.id.buildingStatus);
        priceText.setText(status);

        ImageView iv = (ImageView) findViewById(R.id.buildingImage);
        Bitmap bitmap = getBitmapFromAsset(building.getBuildingId());
        iv.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromAsset(String productId) {
        AssetManager assetManager = getAssets();
        InputStream stream = null;

        try {
            stream = assetManager.open(productId + ".png");
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
