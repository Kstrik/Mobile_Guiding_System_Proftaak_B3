package com.example.breda_op_stap.logic;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.breda_op_stap.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RouteParserTest {

    @Test
    public void parseFile()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        ArrayList<Waypoint> testWaypoints = new ArrayList<Waypoint>();
        testWaypoints.add(new Waypoint("Test1", new LatLng(50.48, 4.5), "Test1 description",
                new ArrayList<String>(){{add("picture1"); add("picture2");}}, false, false, false));
        testWaypoints.add(new Waypoint("Test2", new LatLng(51.86, 4.6), "Test2 description",
                new ArrayList<String>(){{add("picture3");}}, false, false, false));

        RouteParser routeParser = new RouteParser(appContext);
        ArrayList<Waypoint> waypoints = routeParser.parseFile(routeParser.loadJSONFromAsset("JsonRouteTest"));

        if(testWaypoints.size() == waypoints.size())
        {
            for(int i = 0; i < testWaypoints.size(); i++)
            {
                if(testWaypoints.get(i).getName().equals(waypoints.get(i).getName()) && testWaypoints.get(i).getDescription().equals(waypoints.get(i).getDescription()) &&
                        testWaypoints.get(i).getLocation().latitude == waypoints.get(i).getLocation().latitude && testWaypoints.get(i).getLocation().longitude == waypoints.get(i).getLocation().longitude)
                {
                    assert true;
                }
                else
                    assert false;
            }
        }
        else
            assert false;
    }
}