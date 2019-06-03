package com.upgrad.uberclone;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedPref {

    private static SharedPref sharedPref = null;
    private SharedPreferences sharedPreference;

    private static final String TRIPS = "trips";

    private SharedPref(Context context) {
        sharedPreference = context.getSharedPreferences("uberclone", Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance(Context context) {
        if (sharedPref == null) {
            sharedPref = new SharedPref(context);
        }
        return sharedPref;
    }

    public void saveTrips(TripModel trip) {

        List<TripModel> trips = getAllTrip();
        if (trips == null) {
            trips = new ArrayList<>();
            trips.add(trip);
        } else {
            trips.add(trip);
        }

        SharedPreferences.Editor editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(trips);
        editor.putString(TRIPS, json);
        editor.apply();

    }

    public List<TripModel> getAllTrip() {

        Gson gson = new Gson();
        String json = sharedPreference.getString(TRIPS, "");

        List<TripModel> trips = new ArrayList<>();

        if (json.length() > 0) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    trips.add(gson.fromJson(jsonArray.get(i).toString(), TripModel.class));
                }

            } catch (JSONException e) {
                Log.d("error ", e.getMessage());
            }

            return trips;

        }

        return null;

    }

}
