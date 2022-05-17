package com.mobileapp.smartparkingsystem;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {

    private TextView amountTv, bookingTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        findViewById(R.id.topup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("amount",0);
                amount+=500;
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("amount",amount).commit();

                showBalance();
            }
        });



        amountTv = findViewById(R.id.ewallet_tv_amount);
        bookingTv = findViewById(R.id.ewallet_tv_bookings);

        showBalance();

        String booking = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("booking", "No Parking booked yet");
        bookingTv.setText(booking);
    }

    private void showBalance(){
        int amount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("amount",0);
        amountTv.setText("Balance: $" + amount);
    }
}