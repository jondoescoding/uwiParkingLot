package com.mobileapp.smartparkingsystem;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobileapp.smartparkingsystem.adapters.ParkingListviewAdapter;
import com.mobileapp.smartparkingsystem.models.ParkingSpace;

import java.util.ArrayList;
import java.util.Objects;

public class ListMainActivity extends AppCompatActivity {

    ListView parkingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        parkingListView = findViewById(R.id.list_lv_parkinglist);


        ArrayList<ParkingSpace> parkingsList = new ArrayList<ParkingSpace>();


        String[] names = getResources().getStringArray(R.array.car);
        for(int i=0; i<names.length; i++){
            int totalCount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("tp"+(i+1),50);
            int availableCount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("p"+(i+1),totalCount);
            parkingsList.add(
                    new ParkingSpace(names[i], "Available parking count: " + availableCount +"\nTotal parking count: " + totalCount, "p"+(i+1))
            );

        }


        ParkingListviewAdapter adapter = new ParkingListviewAdapter(this, parkingsList);

        parkingListView.setAdapter(adapter);

    }



}