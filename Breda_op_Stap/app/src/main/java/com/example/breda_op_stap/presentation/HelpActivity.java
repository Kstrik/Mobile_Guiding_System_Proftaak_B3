package com.example.breda_op_stap.presentation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.breda_op_stap.R;

public class HelpActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ((TextView)findViewById(R.id.help)).setText(getApplicationContext().getString(R.string.instructionsHeader));
        ((TextView)findViewById(R.id.informatie)).setText(getApplicationContext().getString(R.string.help_description));
    }
}