package com.upgrad.uberclone;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapListener{

    private GoogleMap map;


    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private MapHelper mapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapHelper = new MapHelper(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mapHelper.getLastKnowLocation());
            super.onSaveInstanceState(outState);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        this.map.setMyLocationEnabled(true);
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.getUiSettings().setCompassEnabled(true);
        this.map.getUiSettings().setTiltGesturesEnabled(true);

        mapHelper.getDeviceLocation();


    }

    @Override
    public void deviceLocation(LatLng latLng, int zoom, boolean status) {
        if(status) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }else{
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    public void openNavigation(View view) {
        //TODO open navigation

    }

    public void startSearch(View view) {
        Location currentLocation = (Location) mapHelper.getLastKnowLocation();
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constants.LATITUDE, currentLocation.getLatitude());
        intent.putExtra(Constants.LONGITUDE, currentLocation.getLongitude());
        startActivityForResult(intent, Constants.SEARCH_REQUEST_CODE);
    }
}