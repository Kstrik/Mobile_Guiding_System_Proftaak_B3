package com.example.breda_op_stap.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.DragAndDropPermissionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;
import com.example.breda_op_stap.logic.RouteParser;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback, MenuFragment.OnFragmentInteractionListener
{
    private GoogleMap googleMap;
    private FusedLocationProviderClient fushedLocationProviderClient;
    private GeofencingClient geofencingClient;

    private LocationCallback locationCallback;
    //private ArrayList<Marker> markers;
    private HashMap<Marker, Waypoint> waypointMarkers;

    private EditText txb_Search;
    private View menuFragment;

    private Waypoint withinRange;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        //this.markers = new ArrayList<Marker>();
        this.waypointMarkers = new HashMap<Marker, Waypoint>();
        this.txb_Search = (EditText)findViewById(R.id.txb_SearchMarker);
        this.menuFragment = findViewById(R.id.menuFragment);

        this.menuFragment.setVisibility(View.INVISIBLE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkLocationPremissions();
    }

    private boolean hasLocationAccess()
    {
        boolean courceLocationAccess = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fineLocationAccess = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        return (courceLocationAccess && fineLocationAccess);
    }

    private void checkLocationPremissions()
    {
        if (!hasLocationAccess())
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
        else
            setupLocationServices();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                setupLocationServices();
                this.googleMap.setMyLocationEnabled(true);
            }
            else
                Toast.makeText(this, R.string.premissions_location_denied, Toast.LENGTH_LONG).show();
        }
    }

    private void setupLocationServices()
    {
        this.fushedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000).setFastestInterval(5000).setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        this.locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult == null)
                    return;

                for (Location location : locationResult.getLocations())
                {
                    if (location != null)
                    {
                        Log.d("locationUpdate", location.getLatitude() + " : " + location.getLongitude());
                        onLocationReceived(location);
                    }
                }
            }
        };
        this.fushedLocationProviderClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
    }

    private void onLocationReceived(Location location)
    {
        if(this.googleMap != null)
        {
            boolean waypointsInRange = false;
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 50));
            for(Waypoint waypoint : this.waypointMarkers.values())
            {
                if(getDistance(new LatLng(location.getLatitude(), location.getLongitude()), waypoint.getLocation()) < 100)
                {
                    waypointsInRange = true;
                    if(this.withinRange == null || !waypoint.getName().equals(this.withinRange.getName()))
                    {
                        this.withinRange = waypoint;
                        Log.d("locationUpdate", this.withinRange.hashCode() + " : " + waypoint.hashCode());
                        Toast.makeText(this, waypoint.getName() + " : within 100 meters!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            if(this.withinRange != null && !waypointsInRange)
                this.withinRange = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        if(hasLocationAccess())
            this.googleMap.setMyLocationEnabled(true);
        //this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                onMarkerClicked(marker);
                return true;
            }
        });

        //--------TEST DATA--------
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        for(int i = 0; i < 10; i++)
            waypoints.add(new Waypoint("Test" + i, new LatLng(i, 10), "Test", new ArrayList<String>(), (i >= 9 && i <= 9), (i >= 2 && i <= 4), (i >= 5 && i <= 8)));
        displayRoute(waypoints);
        //-------------------------
    }

    public void displayRoute(ArrayList<Waypoint> waypoints)
    {
        if(waypoints != null && waypoints.size() != 0)
        {
            this.waypointMarkers.clear();
            this.googleMap.clear();
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
                    this.waypointMarkers.put(marker, waypoint);
                }
                //marker.setTag(0);
                polylineOptions.add(waypoint.getLocation());
            }

            this.googleMap.addPolyline(polylineOptions);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(waypoints.get(0).getLocation()));
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waypoints.get(0).getLocation(), 50));
        }
    }

    public void addWaypoint(Waypoint waypoint)
    {
        if(this.googleMap != null)
        {

        }
    }

    private void onMarkerClicked(Marker marker)
    {
        startActivity(new Intent(this, DetailedActivity.class).putExtra("waypoint", this.waypointMarkers.get(marker)));
    }

    public void onSearchMarkerClick(View view)
    {
        if(this.txb_Search.getText() != null && !this.txb_Search.getText().toString().equals(""))
        {
            String markerName = this.txb_Search.getText().toString();

            for(Marker marker : this.waypointMarkers.keySet())
            {
                if(marker.getTitle().toLowerCase().startsWith(markerName))
                {
                    this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, R.string.no_markers_found, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, R.string.marker_search_empty, Toast.LENGTH_LONG).show();
    }

    public void onMenuClick(View view)
    {
        if(this.menuFragment.getVisibility() == View.VISIBLE)
            this.menuFragment.setVisibility(View.INVISIBLE);
        else
            this.menuFragment.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    public void onFavoritesClick(View view)
    {
        startActivity(new Intent(this, POIActivity.class));
    }

    public void onLanguageClick(View view)
    {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        switch(view.getTag().toString())
        {
            case "nl":
            case "us":
            {
                //conf.setLocale(new Locale(view.getTag().toString().toLowerCase()));
                //res.updateConfiguration(conf, dm);
                break;
            }
            default:
            {
                Toast.makeText(this, R.string.language_not_supported, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    public void onInfoClick(View view)
    {
        //TODO uncomment when HelpActivity is added
        startActivity(new Intent(this, HelpActivity.class));
    }

    //Returns between two point in meters
    private double getDistance(LatLng pointA, LatLng pointB)
    {
        double radius = 6371e3;

        double phi1 = Math.toRadians(pointA.latitude), alp1 = Math.toRadians(pointA.longitude);
        double phi2 = Math.toRadians(pointB.latitude), alp2 = Math.toRadians(pointB.longitude);
        double deltaPhi = phi2 - phi1;
        double deltaAlpha = alp2 - alp1;

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
                    + Math.cos(phi1) * Math.cos(phi2)
                    * Math.sin(deltaAlpha / 2) * Math.sin(deltaAlpha / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = radius * c;

        return d;
    }
}
