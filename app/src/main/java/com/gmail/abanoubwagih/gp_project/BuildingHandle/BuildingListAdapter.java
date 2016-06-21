package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.abanoubwagih.gp_project.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BuildingListAdapter extends ArrayAdapter<Building> {

    private List<Building> products;

    public BuildingListAdapter(Context context, int resource, List<Building> objects) {
        super(context, resource, objects);
        products = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        Building building = products.get(position);

        TextView nameText = (TextView) convertView.findViewById(R.id.itemBuildingName);
        nameText.setText(building.getName());

        // // TODO: 6/20/2016 replace the true or false with color  
        String status = String.valueOf(building.getStatus());
        TextView priceText = (TextView) convertView.findViewById(R.id.itemBuildingStatus);
        priceText.setText(status);

        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap bitmap = getBitmapFromAsset(building.getBuildingId());
        iv.setImageBitmap(bitmap);

        return convertView;
    }

    // this demoy code just to handle photo no more
    //// TODO: 6/20/2016 replace with retrive data 
    private Bitmap getBitmapFromAsset(String buildingId) {
        AssetManager assetManager = getContext().getAssets();
        InputStream stream = null;

        try {
            stream = assetManager.open(buildingId + ".png");
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
