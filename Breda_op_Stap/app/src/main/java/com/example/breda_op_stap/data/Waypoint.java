package com.example.breda_op_stap.data;

import android.media.Image;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Waypoint implements Serializable
{
    transient private LatLng location;
    private String description;
    private String name;
    private ArrayList<String> images;
    private boolean isVisited;
    private boolean isFavorite;
    private boolean isHidden;

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

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public boolean isVisited() {
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

    public void setIsFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public void setIsHidden(boolean hidden) {
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

    public void setIsVisited(boolean visited) {
        this.isVisited = visited;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
