package com.upgrad.uberclone;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapListener {

    private GoogleMap map;


    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private MapHelper mapHelper;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;

    private String source;
    private String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapHelper = new MapHelper(this);

        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

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
        if (status) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {

            double latitiude = data.getDoubleExtra(Constants.LATITUDE, 0.0);
            double longitude = data.getDoubleExtra(Constants.LONGITUDE, 0.0);
            source = data.getStringExtra(Constants.SOURCE);
            destination = data.getStringExtra(Constants.DESTINATION);

            getPath(latitiude, longitude);

        }

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(navigationView)) {
            drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void getPath(double desLatitude, double desLongitude) {

        //TOdo handle api access issue

        Location location = (Location) mapHelper.getLastKnowLocation();
        String str_origin = "origin=" + location.getLatitude() + "," + location.getLongitude();
        String str_dest = "destination=" + desLatitude + "," + desLongitude;
        String sensor = "sensor=false";
        String key = getResources().getString(R.string.google_maps_key);
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        new FetchUrl().execute(url);
    }

    public void openNavigation(View view) {
        drawer.openDrawer(GravityCompat.START);
    }

    public void startSearch(View view) {
        Location currentLocation = (Location) mapHelper.getLastKnowLocation();
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constants.LATITUDE, currentLocation.getLatitude());
        intent.putExtra(Constants.LONGITUDE, currentLocation.getLongitude());
        startActivityForResult(intent, Constants.SEARCH_REQUEST_CODE);
    }

    public void navigationItemClicked(View view) {
        TextView v = (TextView) view;
        switch (String.valueOf(v.getTag())) {
            case "1":
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case "2":
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.uber.com/"));
                startActivity(browserIntent);
                break;
            case "3":
                finish();
                break;
        }

        onBackPressed();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void payRide(View view) {

        //TODO proceed the payment
        showToast("Payment done");
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        FragmentManager fm = getSupportFragmentManager();
        CompleteRide completeRide = CompleteRide.newInstance(source, destination);
        completeRide.setCancelable(false);
        completeRide.show(fm, "fragment_ride");

    }

    private class FetchUrl extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            return NetworkFetcher.makeServiceCall(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(strings[0]);
                Log.d("ParserTask", strings[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = lists.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(getResources().getColor(R.color.grey_700));

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            map.clear();
            if (lineOptions != null) {
                map.addPolyline(lineOptions);
                startRideInfo();
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }

    }

    private void startRideInfo() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

}