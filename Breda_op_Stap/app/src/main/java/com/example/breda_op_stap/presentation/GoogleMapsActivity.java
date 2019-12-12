package com.example.breda_op_stap.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.DragAndDropPermissionsCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private FusedLocationProviderClient fushedLocationProviderClient;

    private LocationCallback locationCallback;
    private ArrayList<Marker> markers;

    private EditText txb_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        this.markers = new ArrayList<Marker>();
        this.txb_Search = (EditText)findViewById(R.id.txb_SearchMarker);

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
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 50));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        if(hasLocationAccess())
            this.googleMap.setMyLocationEnabled(true);
        //this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //--------TEST DATA--------
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
        for(int i = 0; i < 10; i++)
            waypoints.add(new Waypoint("Test" + i, new LatLng(i, 10), "Test", null, (i >= 9 && i <= 9), (i >= 2 && i <= 4), (i >= 5 && i <= 8)));
        displayRoute(waypoints);
        //-------------------------
    }

    public void displayRoute(ArrayList<Waypoint> waypoints)
    {
        if(waypoints != null && waypoints.size() != 0)
        {
            this.markers.clear();
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
                    this.markers.add(marker);
                }
                //marker.setTag(0);
                polylineOptions.add(waypoint.getLocation());
            }

            this.googleMap.addPolyline(polylineOptions);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(waypoints.get(0).getLocation()));
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waypoints.get(0).getLocation(), 50));
        }
    }

    public void onSearchMarkerClick(View view)
    {
        if(this.txb_Search.getText() != null && !this.txb_Search.getText().toString().equals(""))
        {
            String markerName = this.txb_Search.getText().toString();

            for(Marker marker : markers)
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
}
