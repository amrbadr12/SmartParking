package com.smartparking.amrbadr12.smartparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Toolbar primaryToolbar;
    private Toolbar cardviewToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        primaryToolbar= (Toolbar) findViewById(R.id.primary_toolbar);
        cardviewToolbar=(Toolbar)findViewById(R.id.card_view_toolbar);
        setSupportActionBar(cardviewToolbar);
        getSupportActionBar().setTitle("Smart Parking");
    }
}
