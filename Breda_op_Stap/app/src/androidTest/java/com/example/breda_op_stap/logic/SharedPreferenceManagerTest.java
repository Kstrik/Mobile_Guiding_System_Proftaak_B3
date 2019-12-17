package com.example.breda_op_stap.logic;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.breda_op_stap.MainActivity;
import com.example.breda_op_stap.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static com.example.breda_op_stap.logic.SharedPreferenceManager.SHARED_PREFS;
import static org.junit.Assert.*;

public class SharedPreferenceManagerTest
{
    @Test
    public void getIsHidden()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(appContext);

        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS, appContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Waypoint waypoint = new Waypoint("Test", new LatLng(0, 0), "Test", null, false, false, false);
        editor.putBoolean(waypoint.getName() + "Hidden", true);
        editor.apply();

        boolean isHidden = sharedPreferenceManager.getIsHidden(waypoint);

        assertEquals(isHidden, true);
    }

    @Test
    public void getIsFavourite()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(appContext);

        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS, appContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Waypoint waypoint = new Waypoint("Test", new LatLng(0, 0), "Test", null, false, false, false);
        editor.putBoolean(waypoint.getName() + "Favourite", true);
        editor.apply();

        boolean isFavorite = sharedPreferenceManager.getIsFavourite(waypoint);

        assertEquals(isFavorite, true);
    }

    @Test
    public void setIsHidden()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(appContext);

        Waypoint waypoint = new Waypoint("Test", new LatLng(0, 0), "Test", null, false, false, false);
        sharedPreferenceManager.setIsHidden(waypoint, true);

        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS, appContext.MODE_PRIVATE);
        boolean isHidden = sharedPreferences.getBoolean(waypoint.getName() + "Hidden", false);

        assertEquals(isHidden, true);
    }

    @Test
    public void setIsFavourite()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(appContext);

        Waypoint waypoint = new Waypoint("Test", new LatLng(0, 0), "Test", null, false, false, false);
        sharedPreferenceManager.setIsFavourite(waypoint, true);

        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS, appContext.MODE_PRIVATE);
        boolean isFavourite = sharedPreferences.getBoolean(waypoint.getName() + "Favourite", false);

        assertEquals(isFavourite, true);
    }
}