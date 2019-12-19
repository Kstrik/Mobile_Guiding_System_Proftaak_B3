package com.example.breda_op_stap.data;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WaypointTest
{
    @Test
    public void waypointCreation()
    {
        Waypoint waypoint = new Waypoint("Test", new LatLng(50.48, 4.5), "Test description",
                new ArrayList<String>(){{add("picture1"); add("picture2");}}, false, false, false);

        if(waypoint.getName().equals("Test") && waypoint.getDescription().equals("Test description") &&
                waypoint.getLocation().latitude == 50.48 && waypoint.getLocation().longitude == 4.5 &&
                !waypoint.isVisited() && !waypoint.isFavorite() && !waypoint.isHidden())
            assert true;
        else
            assert false;
    }
}