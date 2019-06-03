package com.upgrad.uberclone;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private List<TripModel> trips = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ride_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        TripModel trip = trips.get(i);
        String fare = "Fare Rs." + ((int)(Math.random() * (16 - 4) + 1) + 4);
        viewHolder.fare.setText(fare);
        viewHolder.time.setText(getTime(trip.getTime()));
        viewHolder.source.setText(trip.getSource());
        viewHolder.destination.setText(trip.getDestination());
    }


    private String getTime(Long seconds) {
        long timeMilli = new Date().getTime();
        long currentSecond = timeMilli / 1000;
        seconds = currentSecond - seconds;
        if (seconds > 59) {
            long minutes = seconds / 60;
            if (minutes > 59) {
                long hours = minutes / 60;
                if (hours > 23) {
                    int days = (int) hours / 24;
                    if (days > 364) {
                        int year = days / 365;
                        return String.valueOf(year) + " years ago";
                    } else {
                        return String.valueOf(days) + " days ago";
                    }
                } else {
                    return String.valueOf(hours) + " hours ago";
                }
            } else {
                return String.valueOf(minutes) + " mins ago";
            }
        } else {
            return String.valueOf(seconds) + " seconds ago";
        }
    }

    @Override
    public int getItemCount() {
        return trips == null ? 0 : trips.size();
    }

    public void swap(List<TripModel> allTrip) {
        this.trips = allTrip;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fare, time, source, destination;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fare = itemView.findViewById(R.id.fareTV);
            time = itemView.findViewById(R.id.timeTV);
            source = itemView.findViewById(R.id.sourceTV);
            destination = itemView.findViewById(R.id.destinationTV);
        }
    }
}
