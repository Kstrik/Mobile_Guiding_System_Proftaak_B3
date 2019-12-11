package com.example.breda_op_stap.data;

import android.provider.MediaStore;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Waypoint
{
    LatLng location;
    String description;
    String name;
    List<MediaStore.Images> images;
    boolean isVisited;
    boolean isFavorite;
    boolean isHidden;

    public Waypoint(String name, LatLng position, String desc, List<MediaStore.Images> images, boolean isVisited, boolean isFavorite, boolean isHidden )
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
