package com.mobileapp.smartparkingsystem.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileapp.smartparkingsystem.R;
import com.mobileapp.smartparkingsystem.models.ParkingSpace;

import java.util.ArrayList;

public class ParkingListviewAdapter extends ArrayAdapter<ParkingSpace> {
    Context context;
    public ParkingListviewAdapter(Context context, ArrayList<ParkingSpace> parkingSpaceArrayList) {
        super(context, 0, parkingSpaceArrayList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ParkingSpace parkingSpace = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.parking_item, parent, false);
        }

        TextView nameTv = convertView.findViewById(R.id.item_tv_name);
        TextView countTv =  convertView.findViewById(R.id.item_tv_count);
        ImageView parkingIv = convertView.findViewById(R.id.item_iv_parking);

        nameTv.setText(parkingSpace.name);
        countTv.setText(parkingSpace.count);

        String uri = "@drawable/"+parkingSpace.img;  // where myresource (without the extension) is the file

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());


        Drawable res = context.getResources().getDrawable(imageResource);
        parkingIv.setImageDrawable(res);

        return convertView;
    }
}