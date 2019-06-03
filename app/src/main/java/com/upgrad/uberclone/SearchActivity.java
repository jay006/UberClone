package com.upgrad.uberclone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private TextView sourceTV;
    private EditText destinationET;

    private double latitude = 0.00;
    private double longitude = 0.00;

    private String completeAddress;
    private String destination;

    private List<Address> addresses = new ArrayList<>();
    private static final String TAG = SearchActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(Constants.LATITUDE, 0);
        longitude = intent.getDoubleExtra(Constants.LONGITUDE, 0);

        sourceTV = findViewById(R.id.sourceTV);

        getNameFromCoordinate();
        initListener();

    }

    private void initListener() {

        Places.initialize(this, getResources().getString(R.string.google_maps_key), Locale.ENGLISH);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Where to?");
        autocompleteFragment.setCountry("IN");
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                destination = place.getName();
                getListOfAddress(place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void getListOfAddress(String query) {

        addresses.clear();
        Geocoder geocoder = new Geocoder(this);

        try {
            addresses = geocoder.getFromLocationName(query, 1);
        } catch (IOException e) {
            Log.d(SearchActivity.class.getCanonicalName(), e.getMessage());
        }

        if (addresses != null && addresses.size() > 0) {

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.LATITUDE, addresses.get(0).getLatitude());
            resultIntent.putExtra(Constants.LONGITUDE, addresses.get(0).getLongitude());
            resultIntent.putExtra(Constants.SOURCE, completeAddress);
            resultIntent.putExtra(Constants.DESTINATION, destination);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }

    }

    private void getNameFromCoordinate() {

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                completeAddress = address.getSubAdminArea() + ", " + address.getLocality() + ", " + address.getAdminArea() + ", " + address.getCountryName() + " - " + address.getPostalCode();
                sourceTV.setText(completeAddress.trim());
            }
        } catch (IOException e) {
            Log.d(SearchActivity.class.getCanonicalName(), e.getMessage());
        }

    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }
}
