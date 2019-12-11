package com.example.breda_op_stap.logic;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIViewHolder>{

    private ArrayList<Waypoint> waypoints;

    public POIAdapter(ArrayList<Waypoint> waypoints)
    {

        this.waypoints = waypoints;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new POIViewHolder(inflater.inflate(R.layout.activity_poi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {

        Waypoint waypoint = this.waypoints.get(position);

    }

    @Override
    public int getItemCount() {

        return this.waypoints.size();
    }

    public class POIViewHolder extends RecyclerView.ViewHolder {

        public POIViewHolder(@NonNull View itemView) {

            super(itemView);

            itemView.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {

            // TODO: // null should become DetailedActivity
             Intent intent = new Intent(view.getContext(), null);
             intent.putExtra("WAYPOINT", (Parcelable) this.getCurrentWaypoint());

             view.getContext().startActivity(intent);
        }

        private Waypoint getCurrentWaypoint() {

            return waypoints.get(POIViewHolder.super.getAdapterPosition());
        }
    }
}
