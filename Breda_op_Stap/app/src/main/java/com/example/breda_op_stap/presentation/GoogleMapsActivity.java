package com.example.breda_op_stap.presentation;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

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
        checkLocationPremissions();
    }

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
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000).setFastestInterval(500).setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        final AlertDialog alert = new AlertDialog.Builder(this).setTitle("Location received!").create();
//
//        this.fushedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    Log.d("locationReceived", location.getLatitude() + " : " + location.getLongitude());
//                }
//                else {d
//                    alert.show();
//                    Log.d("locationReceived", "No location received!");
//                }
//            }
//        });

        this.locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                Log.d("locationReceived", "Location update!");
                if (locationResult == null)
                {
                    Log.d("locationReceived", "No location received!");
                    return;
                }

                for (Location location : locationResult.getLocations())
                {
                    //alert.show();
                    if (location != null) {
                        Log.d("locationReceived", location.getLatitude() + " : " + location.getLongitude());
                    }
                }
            }
        };
        this.fushedLocationProviderClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        //this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        for(int i = 0; i < 10; i++)
            waypoints.add(new Waypoint("test", new LatLng(i, 10), "Test", null, (i >= 9 && i <= 9), (i >= 2 && i <= 4), (i >= 5 && i <= 8)));
        displayRoute(waypoints);
    }

    public void displayRoute(ArrayList<Waypoint> waypoints)
    {
        if(waypoints != null && waypoints.size() != 0)
        {
            PolylineOptions polylineOptions = new PolylineOptions().clickable(false);

            for(Waypoint waypoint : waypoints)
            {
                if(!waypoint.isHidden())
                {
                    MarkerOptions markerOptions = new MarkerOptions().position(waypoint.getLocation()).title(waypoint.getName());
                    if(waypoint.isVisited())
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    if(waypoint.isFavorite())
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    Marker marker = this.googleMap.addMarker(markerOptions);
                }
                //marker.setTag(0);
                polylineOptions.add(waypoint.getLocation());
            }

            this.googleMap.addPolyline(polylineOptions);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(waypoints.get(0).getLocation()));
        }
    }
}
