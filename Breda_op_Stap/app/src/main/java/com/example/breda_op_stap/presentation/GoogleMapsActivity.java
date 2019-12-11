package com.example.breda_op_stap.presentation;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.RouteParser;
import com.example.breda_op_stap.data.Waypoint;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private FusedLocationProviderClient fushedLocationProviderClient;

    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        RouteParser routeParser = new RouteParser();
        String json = routeParser.loadJSONFromAsset(this, "JsonRoute");
        routeParser.parseFile(json);
        Log.d("PARSED", routeParser.parseFile(json).toString());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //checkLocationPremissions();
    }

/*
    private void checkLocationPremissions()
    {
        boolean permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionAccessCoarseLocationApproved)
        {
            setupLocationServices();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
        }
    }

    private void setupLocationServices()
    {
        this.fushedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        this.locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult == null)
                    return;

                for (Location location : locationResult.getLocations())
                {
                    if (location != null) {
                        Log.i("locationReceived", location.getLatitude() + " : " + location.getLongitude());
                    }
                }
            }
        };
        this.fushedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
*/

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        for(int i = 0; i < 10; i++)
            waypoints.add(new Waypoint("test", new LatLng(i, 10), "Test", null, false, false, false));
        displayRoute(waypoints);
    }

    public void displayRoute(ArrayList<Waypoint> waypoints)
    {
        if(waypoints != null && waypoints.size() != 0)
        {
            PolylineOptions polylineOptions = new PolylineOptions().clickable(false);

            for(Waypoint waypoint : waypoints)
            {
                Marker marker = this.googleMap.addMarker(new MarkerOptions().position(waypoint.getLocation()).title(waypoint.getDescription()));
                marker.setTag(0);
                polylineOptions.add(waypoint.getLocation());
            }

            this.googleMap.addPolyline(polylineOptions);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(waypoints.get(0).getLocation()));
        }
    }
}
