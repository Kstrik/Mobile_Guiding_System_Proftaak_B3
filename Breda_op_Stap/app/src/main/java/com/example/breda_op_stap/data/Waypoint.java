package com.example.breda_op_stap.data;

import android.media.Image;
import android.provider.MediaStore;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Waypoint
{
    LatLng location;
    String description;
    String name;
    ArrayList<String> images;
    boolean isVisited;
    boolean isFavorite;
    boolean isHidden;

    public Waypoint(String name, LatLng position, String desc, ArrayList<String> images, boolean isVisited, boolean isFavorite, boolean isHidden )
    {
        this.name = name;
        this.location = position;
        this.description = desc;
        this.images = images;
        this.isVisited = isVisited;
        this.isFavorite = isFavorite;
        this.isHidden = isHidden;
    }

    public boolean getFavorite() {
        return this.isFavorite;
    }

    public boolean getHidden() {
        return this.isHidden;
    }

    public boolean getVisited() {
        return this.isVisited;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getImages() {
        return this.images;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public void setHidden(boolean hidden) {
        this.isHidden = hidden;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
