package com.example.breda_op_stap.logic;

import com.example.breda_op_stap.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface DirectionsAPIListener
{
    void onRouteAvailable(ArrayList<LatLng> locations, ArrayList<Waypoint> waypoints);
}
