package com.example.breda_op_stap.logic;

import android.content.Context;
import com.example.breda_op_stap.data.Waypoint;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;

public class RouteParser
{
    Context context;
    //RequestQueue requestQueue;

    public RouteParser(Context context)
    {
        this.context = context;
        //this.requestQueue = Volley.newRequestQueue(this.context);
    }

    public String loadJSONFromAsset(String file)
    {
        String json = null;
        try
        {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public ArrayList<Waypoint> parseFile(String JSONstring, String landcode)
    {
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        try
        {
            JSONArray obj = new JSONArray(JSONstring);
            for (int i = 0; i < obj.length(); i++)
            {
                String name = obj.getJSONObject(i).getString("name");
                LatLng position = new LatLng(
                        obj.getJSONObject(i).getJSONObject("coordinates").getDouble("lat"),
                        obj.getJSONObject(i).getJSONObject("coordinates").getDouble("lng")
                );
                String descUS = obj.getJSONObject(i).getString("descriptionUS");
                String descNL = obj.getJSONObject(i).getString("descriptionNL");
                ArrayList<String> images = new ArrayList<String>();
                for (int z = 0; z < obj.getJSONObject(i).getJSONArray("images").length(); z++)
                {
                    images.add(obj.getJSONObject(i).getJSONArray("images").getString(z));
                }
                boolean isVisited = obj.getJSONObject(i).getBoolean("isVisited");
                boolean isFavorite = obj.getJSONObject(i).getBoolean("isFavorite");
                boolean isHidden = obj.getJSONObject(i).getBoolean("isHidden");

                Waypoint waypoint = (new Waypoint(name, position, (landcode.equals("nl") ? descNL : descUS), images, isVisited, isFavorite, isHidden));
                SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(this.context);
                waypoint.setIsFavorite(sharedPreferenceManager.getIsFavourite(waypoint));
                waypoint.setIsHidden(sharedPreferenceManager.getIsHidden(waypoint));
                waypoints.add(waypoint);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return waypoints;
    }
}