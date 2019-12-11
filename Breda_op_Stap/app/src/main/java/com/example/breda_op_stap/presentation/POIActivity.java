package com.example.breda_op_stap.presentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.RouteParser;
import com.example.breda_op_stap.data.Waypoint;
import com.example.breda_op_stap.logic.POIAdapter;
import com.example.breda_op_stap.logic.POIListener;

import java.util.ArrayList;

public class POIActivity extends AppCompatActivity implements POIListener {

    private ArrayList<Waypoint> waypoints;
    private POIAdapter poiAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

        this.waypoints = new ArrayList<>();
        this.poiAdapter = new POIAdapter(this.waypoints);

        RecyclerView recyclerView = findViewById(R.id.poi_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.poiAdapter);

        // TODO: Triggering the function should start getting waypoints and callback on onPOIAvailable.
        // RouteParser routeParser = new RouteParser();
        // routeParser.parseFile("waypoints.txt", this);

        // TODO: TIP: the following is possible
        // function (Context context) {
        //      this.listener = (POIListener) context;
        // }
    }

    @Override
    public void onPOIAvailable(Waypoint waypoint) {

        this.waypoints.add(waypoint);
        this.poiAdapter.notifyDataSetChanged();
    }
}
