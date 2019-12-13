package com.example.breda_op_stap.presentation;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;
import com.example.breda_op_stap.logic.DirectionsAPIListener;
import com.example.breda_op_stap.logic.DirectionsAPIManager;
import com.example.breda_op_stap.logic.Notification;
import com.example.breda_op_stap.logic.RouteParser;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback, MenuFragment.OnFragmentInteractionListener, DirectionsAPIListener
{
    private GoogleMap googleMap;

    private FusedLocationProviderClient fushedLocationProviderClient;
    private LocationCallback locationCallback;

    private ArrayList<LatLng> locations;
    private ArrayList<Waypoint> waypoints;
    private HashMap<Marker, Waypoint> waypointMarkers;

    private EditText txb_Search;
    private View menuFragment;
    private Waypoint withinRange;

    private Notification notification;
    private DirectionsAPIManager directionsAPIManager;

    private Polyline polyline;
    private Polyline walkedRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        this.locations = new ArrayList<LatLng>();
        this.waypoints = new ArrayList<Waypoint>();
        this.waypointMarkers = new HashMap<Marker, Waypoint>();

        this.txb_Search = (EditText)findViewById(R.id.txb_SearchMarker);
        this.menuFragment = findViewById(R.id.menuFragment);
        this.menuFragment.setVisibility(View.INVISIBLE);

        this.notification = new Notification(this);
        this.directionsAPIManager = new DirectionsAPIManager(this, this);

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
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
                {
                    return;
                }
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
            Waypoint closestWaypoint = null;
            double closestDistance = 0;

            for(Waypoint waypoint : this.waypointMarkers.values())
            {
                double distance = getDistance(new LatLng(location.getLatitude(), location.getLongitude()), waypoint.getLocation());
                if(distance < 100 && (closestWaypoint == null || distance < closestDistance))
                {
                    closestWaypoint = waypoint;
                    closestDistance = distance;
                }
            }

            if(closestWaypoint != null && (this.withinRange == null || !this.withinRange.getName().equals(closestWaypoint.getName())))
                this.notification.encounteredWaypointNotifier(closestWaypoint.getName(), closestWaypoint.getDescription());

            this.withinRange = closestWaypoint;

            //Test code
            if(this.polyline != null)
            {
                LatLng closestPointOnPolyline = closedPointOnPolyline(new LatLng(location.getLatitude(), location.getLongitude()), this.polyline);

                if(this.walkedRoute != null)
                    this.walkedRoute.remove();

                ArrayList<LatLng> test = getLocationsBefore(closestPointOnPolyline);
                PolylineOptions polylineOptions = new PolylineOptions().clickable(false).addAll(test);
                polylineOptions.color(getResources().getColor(R.color.colorAccent)).width(20);
                this.walkedRoute = this.googleMap.addPolyline(polylineOptions);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        if(hasLocationAccess()) {
            this.googleMap.setMyLocationEnabled(true);
        }

        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                onMarkerClicked(marker);
                return true;
            }
        });

        RouteParser routeParser = new RouteParser(this);
        displayRoute(routeParser.parseFile(routeParser.loadJSONFromAsset("JsonRoute")));
    }

    public void displayRoute(ArrayList<Waypoint> waypoints)
    {
        this.directionsAPIManager.requestRoute(waypoints);
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
        else {
            Toast.makeText(this, R.string.marker_search_empty, Toast.LENGTH_LONG).show();
        }
    }

    public void onMenuClick(View view)
    {
        if(this.menuFragment.getVisibility() == View.VISIBLE) {
            this.menuFragment.setVisibility(View.INVISIBLE);
        }
        else {
            this.menuFragment.setVisibility(View.VISIBLE);
        }
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
        startActivity(new Intent(this, HelpActivity.class));
    }

    @Override
    public void onRouteAvailable(ArrayList<LatLng> locations, ArrayList<Waypoint> waypoints)
    {
        if(waypoints != null && waypoints.size() != 0)
        {
            this.locations.clear();
            this.waypoints.clear();
            this.waypointMarkers.clear();
            this.googleMap.clear();
            this.locations.addAll(locations);
            this.waypoints.addAll(waypoints);

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
            }

            this.polyline = this.googleMap.addPolyline(new PolylineOptions().clickable(false).addAll(locations));
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(waypoints.get(0).getLocation()));
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waypoints.get(0).getLocation(), 50));
        }
    }

    private ArrayList<LatLng> getLocationsBefore(LatLng latLng)
    {
        ArrayList<LatLng> locationsBefore = new ArrayList<LatLng>();

        for(LatLng location : this.locations)
        {
            locationsBefore.add(location);

            if(location.latitude == latLng.latitude && location.longitude == latLng.longitude)
                break;
        }
        return locationsBefore;
    }

    //Returns distance between two points in meters
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

    private LatLng closedPointOnPolyline(LatLng point, Polyline polyline)
    {
        double lat = point.latitude;
        double lng = point.longitude;
        LatLng closest = null;
        double distance = 999999999;

        for(int i = 0; i < polyline.getPoints().size() - 1; i++)
        {
            double a = polyline.getPoints().get(i).latitude;
            double b = polyline.getPoints().get(i).longitude;
            double c = polyline.getPoints().get(i + 1).latitude;
            double d = polyline.getPoints().get(i + 1).longitude;
            double n = (c - a)*(c - a) + (d - b)*(d - b);
            double frac = (n == 1) ? ((lat - a)*(lat - a) + (lng - b)+(d - b)) / n : 0;
            double e = a + (c - a)*frac;
            double f = b + (d - b)*frac;
            double dist = Math.sqrt((lat - e)*(lat - e) + (lng - f)*(lng - f));
            if(distance == 999999999 || distance > dist)
            {
                distance = dist;
                closest = new LatLng(e, f);
            }
        }
        return closest;
    }
}