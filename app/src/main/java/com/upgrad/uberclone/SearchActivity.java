package com.upgrad.uberclone;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private TextView sourceTV;
    private EditText destinationET;

    private LinearLayout moreContainer;

    private double latitude = 0.00;
    private double longitude = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(Constants.LATITUDE, 0);
        longitude = intent.getDoubleExtra(Constants.LONGITUDE, 0);

        sourceTV = findViewById(R.id.sourceTV);
        destinationET = findViewById(R.id.destinationET);
        moreContainer = findViewById(R.id.moreContainer);

        getNameFromCoordinate();


    }

    private void getNameFromCoordinate() {

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String completeAddress = address.getSubAdminArea() + ", " + address.getLocality() + ", " + address.getAdminArea() + ", " + address.getCountryName() + " - " + address.getPostalCode();
                sourceTV.setText(completeAddress.trim());
            }
        } catch (IOException e) {
            Log.d(SearchActivity.class.getCanonicalName(), e.getMessage());
        }

    }

    public void goBack(View view) {
        Intent resultIntent = new Intent();
        setResult(Constants.SEARCH_REQUEST_CODE, resultIntent);
        finish();
    }
}
