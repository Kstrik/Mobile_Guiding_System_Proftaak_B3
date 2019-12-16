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

import java.util.ArrayList;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIViewHolder>
{
    private ArrayList<Waypoint> waypoints;
    private boolean favorite;

    public POIAdapter(ArrayList<Waypoint> waypoints, boolean favorite)
    {

        this.waypoints = waypoints;
        this.favorite = favorite;
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
        holder.latitude.setText(String.valueOf(waypoint.getLocation().latitude));
        holder.longitude.setText(String.valueOf(waypoint.getLocation().longitude));
        holder.name.setText(waypoint.getName());

        String path = "@drawable/" + waypoint.getImages().get(0);
        int res = holder.itemView.getResources().getIdentifier(path, null, holder.itemView.getContext().getPackageName());
        holder.image.setImageResource(res);

        if (!waypoint.isFavorite())
            holder.fav.setImageResource(R.drawable.ic_star_border_black_24dp);

        if (!waypoint.isHidden())
            holder.hidden.setImageResource(R.drawable.ic_visibility_off_black_24dp);
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
        ImageView fav;
        ImageView hidden;

        public POIViewHolder(@NonNull View itemView)
        {

            super(itemView);

            this.latitude = itemView.findViewById(R.id.poi_item_lat);
            this.longitude = itemView.findViewById(R.id.poi_item_long);
            this.name = itemView.findViewById(R.id.poi_item_name);
            this.image = itemView.findViewById(R.id.poi_item_image);
            this.fav = itemView.findViewById(R.id.poi_item_fav);
            this.hidden = itemView.findViewById(R.id.poi_item_hidden);

            this.fav.setOnClickListener(this::onClickFav);
            this.hidden.setOnClickListener(this::onClickHidden);
            itemView.setOnClickListener(this::onClick);
        }

        private void onClickFav(View view)
        {

            Waypoint waypoint = this.getCurrentWaypoint();
            waypoint.setIsFavorite(!waypoint.isFavorite());

            if (favorite)
                waypoints.remove(waypoint);

            notifyDataSetChanged();
        }

        private void onClickHidden(View view)
        {

            Waypoint waypoint = this.getCurrentWaypoint();
            waypoint.setIsHidden(!waypoint.isHidden());

            notifyDataSetChanged();
        }

        private void onClick(View view)
        {
            Intent intent = new Intent(view.getContext(), DetailedActivity.class);
            intent.putExtra("waypoint", this.getCurrentWaypoint());
            view.getContext().startActivity(intent);
        }

        private Waypoint getCurrentWaypoint()
        {
            return waypoints.get(POIViewHolder.super.getAdapterPosition());
        }
    }
}