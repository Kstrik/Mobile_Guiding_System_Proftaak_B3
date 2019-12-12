package com.example.breda_op_stap.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.breda_op_stap.R;
import com.example.breda_op_stap.data.Waypoint;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Waypoint waypoint = (Waypoint)getIntent().getSerializableExtra("waypoint");
        TextView textView = this.findViewById(R.id.poiTitle);
        textView.setText("pindakaas");
    }
}
