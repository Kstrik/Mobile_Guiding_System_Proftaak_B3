package com.example.breda_op_stap.data;

import android.provider.MediaStore;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Waypoint
{
    LatLng location;
    String description;
    List<MediaStore.Images> images;
    boolean isVisited;
    boolean isFavorite;
    boolean isHidden;

    public Waypoint(LatLng position, String desc, List<MediaStore.Images> images, boolean isVisited, boolean isFavorite, boolean isHidden )
    {
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

    public List<MediaStore.Images> getImages() {
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

    public void setImages(List<MediaStore.Images> images) {
        this.images = images;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
