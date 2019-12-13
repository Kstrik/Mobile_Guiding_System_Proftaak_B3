package com.example.breda_op_stap.logic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;
import com.example.breda_op_stap.presentation.DetailedActivity;

import java.io.File;
import java.util.ArrayList;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIViewHolder>
{
    private ArrayList<Waypoint> waypoints;

    public POIAdapter(ArrayList<Waypoint> waypoints)
    {
        this.waypoints = waypoints;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new POIViewHolder(inflater.inflate(R.layout.activity_poi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position)
    {
        Waypoint waypoint = this.waypoints.get(position);
        holder.latitude.setText((int) waypoint.getLocation().latitude);
        holder.longitude.setText((int) waypoint.getLocation().longitude);
        holder.name.setText(waypoint.getName());

        File file = new File(waypoint.getImages().get(0));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount()
    {
        return this.waypoints.size();
    }

    class POIViewHolder extends RecyclerView.ViewHolder
    {
        TextView latitude;
        TextView longitude;
        TextView name;
        ImageView image;

        public POIViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.latitude = itemView.findViewById(R.id.poi_item_lat);
            this.longitude = itemView.findViewById(R.id.poi_item_long);
            this.name = itemView.findViewById(R.id.poi_item_name);
            this.image = itemView.findViewById(R.id.poi_item_image);

            itemView.setOnClickListener(this::onClick);
        }

        private void onClick(View view)
        {

            Intent intent = new Intent(view.getContext(), DetailedActivity.class);
            intent.putExtra("WAYPOINT", (Parcelable) this.getCurrentWaypoint());
            view.getContext().startActivity(intent);
        }

        private Waypoint getCurrentWaypoint()
        {
            return waypoints.get(POIViewHolder.super.getAdapterPosition());
        }
    }
}