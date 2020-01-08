package com.example.breda_op_stap.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
        ImageView back = this.findViewById(R.id.backArrow);

        TextView scrollable = this.findViewById(R.id.poiInfo);

        //Enabling scrolling on TextView.
        scrollable.setMovementMethod(new ScrollingMovementMethod());

        title.setText(waypoint.getName());
        description.setText(waypoint.getDescription());

        back.setOnClickListener(v -> this.finish());

        if(waypoint.getImages().size() != 0)
        {
            String path = "@drawable/" + waypoint.getImages().get(0);
            int res = getResources().getIdentifier(path, null, getPackageName());
            image.setImageResource(res);
        }
    }
}