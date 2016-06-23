package com.gmail.abanoubwagih.gp_project.BuildingHandle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.abanoubwagih.gp_project.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class BuildingListAdapter extends ArrayAdapter<Building> {

    private List<Building> buildings;

    public BuildingListAdapter(Context context, int resource, List<Building> objects) {
        super(context, resource, objects);

        buildings = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        Building building = buildings.get(position);

        TextView nameText = (TextView) convertView.findViewById(R.id.itemBuildingName);
        nameText.setText(building.getName());

        // // TODO: 6/20/2016 replace the true or false with color  
//        String status = String.valueOf(building.isStatus());
        String status = building.isStatus() ? "save" : "FIre";
        TextView state = (TextView) convertView.findViewById(R.id.itemBuildingStatus);
        state.setText(status);

        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
        InputStream stream = new ByteArrayInputStream(Base64.decode(building.getPhoto().getBytes(), Base64.DEFAULT));
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        iv.setImageBitmap(bitmap);

        return convertView;
    }

}
