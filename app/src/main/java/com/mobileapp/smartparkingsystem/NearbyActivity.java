package com.mobileapp.smartparkingsystem;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NearbyActivity extends AppCompatActivity {

    private MapView mapView;
    private GoogleMap googleMap;
    private Location location = null;
    private Location veryFirstLocation = null;
    private Marker currentMarker = null, selectedMarker = null;

    private static final int LOCATION_MIN_UPDATE_TIME = 50;
    private static final int LOCATION_MIN_UPDATE_DISTANCE = 1000;

    AlertDialog.Builder builder ;

    private Button bookBtn;
    TextView countTv;

    double[] latitudes =  {18.0062096, 18.005823, 18.00432, 18.004874, 18.004137, 18.008066, 18.006068, 18.003261,
            18.009056, 18.008203, 18.007059, 18.005189, 18.003939, 18.007276, 18.006078, 18.007224, 18.0063,18.008078};
    double[] longitudes =     {-76.7472919, -76.744766, -76.749587, -76.745806, -76.747051, -76.748841, -76.745451, -76.745947,
            -76.747238, -76.747968, -76.747613, -76.748847, -76.750328,-76.748266,-76.748273, -76.745154, -76.747764,-76.743247};

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            drawMarker(location, getText(R.string.i_am_here).toString());
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        builder = new AlertDialog.Builder(this);

        bookBtn = findViewById(R.id.nearby_btn_book);
        countTv = findViewById(R.id.nearby_tv_count);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBookingDialogue();
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initView(savedInstanceState);
    }
    private void initView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapView_onMapReady(googleMap);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        getCurrentLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(), getText(R.string.provider_failed), Toast.LENGTH_LONG).show();
            } else {
                location = null;
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
                    if(veryFirstLocation==null)
                        veryFirstLocation=location;
                    drawMarker(location, getText(R.string.i_am_here).toString());

                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void mapView_onMapReady(GoogleMap googleMap) {
        //Toast.makeText(NearbyActivity.this, "Mapview ready", Toast.LENGTH_LONG).show();
        this.googleMap = googleMap;

        initMap();
        getCurrentLocation();

    }



    private void drawMarker(Location location, String title) {
        if (this.googleMap != null) {

            if(currentMarker!=null)
                currentMarker.remove();

            LatLng latLngFirst = new LatLng(veryFirstLocation.getLatitude(), veryFirstLocation.getLongitude());

            String[] names = getResources().getStringArray(R.array.car);
            for(int i=0; i<names.length; i++){
//                LatLng ll = getRandomLocation(latLngFirst, 16000);
                LatLng ll = new LatLng(latitudes[i],longitudes[i]);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(ll);

                String titleWithCount = (i+1) +"- " + names[i];
                markerOptions.title(titleWithCount);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                googleMap.addMarker(markerOptions);
            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    bookBtn.setVisibility(View.VISIBLE);
                    countTv.setVisibility(View.VISIBLE);

                    selectedMarker = marker;

                    updateCounts();
                    return false;
                }
            });


            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currentMarker = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }


    }

    private void updateCounts() {
        String name = selectedMarker.getTitle();
        String[] split = name.split("-");
        String parkingNo = split[0];

        int totalCount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("tp"+parkingNo,50);
        int availableCount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("p"+parkingNo,totalCount);

        countTv.setText("Available parking count: "+ availableCount +"\nTotal parking count: "+ totalCount);
    }

    public void showBookingDialogue(){
        String name = selectedMarker.getTitle();
        String[] split = name.split("-");
        String parkingNo = split[0];
        builder.setTitle("Booking Confirmation");
        builder.setMessage("Are you sure want to book parking at "+name+" at cost of 10$?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                int amount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("amount",0);
                int totalCount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("tp"+parkingNo,50);
                int availableCount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("p"+parkingNo,totalCount);
                if(amount>9 && availableCount>0){
                    amount-=10;
                    availableCount--;
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("amount",amount).commit();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("p"+parkingNo,availableCount).commit();
                    String booking = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("booking", "");
                    booking+="=> " + name + "\n\n";
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("booking",booking).commit();


                    Toast.makeText(NearbyActivity.this, "Booked and $10 is deducted from your ewallet\n" +
                            "Your remaining balance is $" + amount  , Toast.LENGTH_LONG).show();
                    updateCounts();
                }else{
                    Toast.makeText(NearbyActivity.this, "Not enough balance in E-wallet or Parking lot is full", Toast.LENGTH_LONG).show();
                }


                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}