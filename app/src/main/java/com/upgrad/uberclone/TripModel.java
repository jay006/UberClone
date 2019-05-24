package com.upgrad.uberclone;

public class TripModel {

    private String source;
    private String destination;
    private long time;

    public TripModel(String source, String destination, long time) {
        this.source = source;
        this.destination = destination;
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public long getTime() {
        return time;
    }
}
