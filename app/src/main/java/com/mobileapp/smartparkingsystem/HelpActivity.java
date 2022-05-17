package com.mobileapp.smartparkingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    String help = "This is an application for finding parking space\nThere are 5 options on the main screen \n" +
            "NEAR BY PARKING ON MAP \n" +
            "NEAR BY PARKING LIST \n" +
            "CHECK BOOKING AND E WALLET \n" +
            "USER PROFILE \n" +
            "GIVE US FEEDBACK \n\n" +
            "Near by parking on map will show the available parking on the map and you can book the parking by selecting the required one. \n" +
            "Near by parking list will show you the list of near by parking in the form of list along with the pictures of the parking \n" +
            "You can check bookings and the remaining balance in the e-wallet from the check booking and ewallet button from main screen \n" +
            "You can also check and update your user profile by selecting the user profile option from the main screen \n" +
            "You can also give us feedback by selecting the feedback button from main screen";

    TextView helpTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpTv = findViewById(R.id.help_tv_help);
        helpTv.setText(help);

    }
}