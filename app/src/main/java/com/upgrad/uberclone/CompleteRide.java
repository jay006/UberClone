package com.upgrad.uberclone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CompleteRide extends DialogFragment {

    private TextView source, destination;
    private Button completeBtn;

    public CompleteRide() {
    }

    public static CompleteRide newInstance(String source, String destination){
        CompleteRide completeRide = new CompleteRide();
        Bundle args = new Bundle();
        args.putString(Constants.SOURCE, source);
        args.putString(Constants.DESTINATION, destination);
        completeRide.setArguments(args);
        return completeRide;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.complete_ride, container, false);

        final Bundle args = getArguments();

        source = v.findViewById(R.id.source_textView);
        destination = v.findViewById(R.id.destination_textView);
        completeBtn = v.findViewById(R.id.completeTrip);

        source.setText(args.getString(Constants.SOURCE));
        destination.setText(args.getString(Constants.DESTINATION));

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long unixTime = System.currentTimeMillis() / 1000L;

                TripModel trip = new TripModel(
                        args.getString(Constants.SOURCE),
                        args.getString(Constants.DESTINATION),
                        unixTime);

                SharedPref.getInstance(getContext()).saveTrips(trip);

                dismiss();
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;

    }
}
