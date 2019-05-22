package com.upgrad.uberclone;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapHelper {

    private static final String TAG = MapHelper.class.getCanonicalName();

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Location lastKnownLocation;

    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;

    private Context context;
    private MapListener mapListener;



    public MapHelper(Context context) {

        this.context = context;
        mapListener = (MapListener) context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

    }

    public void getDeviceLocation() {

        try {
            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener((Activity) context, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        lastKnownLocation = task.getResult();
                        mapListener.deviceLocation(new LatLng(
                                        lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude()),
                                        DEFAULT_ZOOM,
                                        true);
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mapListener.deviceLocation(defaultLocation, DEFAULT_ZOOM, false);

                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    public Parcelable getLastKnowLocation() {
        return lastKnownLocation;
    }
}
