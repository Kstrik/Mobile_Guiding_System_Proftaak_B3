package com.example.breda_op_stap.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;

public class DetailedActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Waypoint waypoint = (Waypoint) getIntent().getSerializableExtra("waypoint");

        TextView title = this.findViewById(R.id.poiTitle);
        TextView description = this.findViewById(R.id.poiInfo);
        ImageView image = this.findViewById(R.id.pointOfInterest_img);

        title.setText(waypoint.getName());
        description.setText(waypoint.getDescription());

        String path = "@drawable/" + waypoint.getImages().get(0);
        int res = getResources().getIdentifier(path, null, getPackageName());
        image.setImageResource(res);
    }
}