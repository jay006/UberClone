package com.upgrad.uberclone;

import com.google.android.gms.maps.model.LatLng;

public interface MapListener {

    void deviceLocation(LatLng latLng, int zoom, boolean status);

}
