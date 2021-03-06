package com.example.breda_op_stap.presentation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;
import com.example.breda_op_stap.logic.POIAdapter;
import com.example.breda_op_stap.logic.RouteParser;

import java.util.ArrayList;

public class POIActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

        RouteParser routeParser = new RouteParser(getApplicationContext());
        ArrayList<Waypoint> unfilteredWaypoints = routeParser.parseFile(routeParser.loadJSONFromAsset("JsonRoute"), getApplicationContext().getResources().getConfiguration().locale.toLanguageTag());
        ArrayList<Waypoint> waypoints = new ArrayList<>();

        boolean fav = getIntent().getBooleanExtra("favorites", false);

        for (Waypoint waypoint : unfilteredWaypoints)
            if (waypoint.isFavorite() || !fav)
                waypoints.add(waypoint);

        if (fav)
            ((TextView)findViewById(R.id.poi_title)).setText(getApplicationContext().getString(R.string.favoritesTitle));
        else
            ((TextView)findViewById(R.id.poi_title)).setText(getApplicationContext().getString(R.string.overviewTitle));

        RecyclerView recyclerView = findViewById(R.id.poi_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new POIAdapter(waypoints, fav, this));
    }
}