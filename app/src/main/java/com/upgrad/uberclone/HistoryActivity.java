package com.upgrad.uberclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.rides);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RidesAdapter ridesAdapter = new RidesAdapter();
        ridesAdapter.swap(SharedPref.getInstance(this).getAllTrip());

        recyclerView.setAdapter(ridesAdapter);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
