package com.example.breda_op_stap.logic;
import android.content.Context;
import com.example.breda_op_stap.data.Waypoint;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.breda_op_stap.logic.POIListener;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class RouteParser {
    Context context;
        RequestQueue requestQueue;
        POIListener listener;

        public RouteParser(Context context, POIListener listener) {
            this.context = context;
            this.requestQueue = Volley.newRequestQueue(this.context);
            this.listener = listener;
        }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public void parseFile(String JSONstring) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                JSONstring,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Waypoint> waypoints = new ArrayList<>();
                        try {
                            JSONArray obj = new JSONArray(JSONstring);
                            for (int i = 0; i < obj.length(); i++) {
                                String name = obj.getJSONObject(i).getString("name");
                                LatLng position = new LatLng(
                                        obj.getJSONObject(i).getJSONObject("coordinates").getInt("lat"),
                                        obj.getJSONObject(i).getJSONObject("coordinates").getInt("lng")
                                );
                                String desc = obj.getJSONObject(i).getString("description");
                                ArrayList<String> images = new ArrayList<String>();
                                for (int z = 0; z < obj.getJSONObject(i).getJSONArray("images").length(); z++) {
                                    images.add(obj.getJSONObject(i).getJSONArray("images").getString(z));
                                }
                                boolean isVisited = obj.getJSONObject(i).getBoolean("isVisited");
                                boolean isFavorite = obj.getJSONObject(i).getBoolean("isFavorite");
                                boolean isHidden = obj.getJSONObject(i).getBoolean("isHidden");

                                waypoints.add(new Waypoint(name, position, desc, images, isVisited, isFavorite, isHidden));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY_TAG", error.toString());
            }
        });
        requestQueue.add(request);
    }
}