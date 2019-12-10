package com.example.breda_op_stap.data;

import android.provider.MediaStore;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Waypoint
{
    LatLng location;
    String description;
    List<MediaStore.Images> images;
    Boolean isVisited;
    Boolean isFavorite;
    Boolean isHidden;

    public Waypoint(LatLng position, String desc, List<MediaStore.Images> images, Boolean isVisited, Boolean isFavorite, Boolean isHidden )
    {
        this.location = position;
        this.description = desc;
        this.images = images;
        this.isVisited = isVisited;
        this.isFavorite = isFavorite;
        this.isHidden = isHidden;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public Boolean getVisited() {
        return isVisited;
    }

    public LatLng getLocation() {
        return location;
    }

    public List<MediaStore.Images> getImages() {
        return Images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

//    public void setImages(List<MediaStore.Images> images) {
//        List<MediaStore.Images> = images;
//    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setVisited(Boolean visited) {
        isVisited = visited;
    }
}
