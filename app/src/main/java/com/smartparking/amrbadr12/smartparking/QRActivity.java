package com.smartparking.amrbadr12.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QRActivity extends AppCompatActivity {
    private FloatingActionButton parkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        TextView hoursTextView;
        String hoursText;
        Toolbar primaryToolbar;
        hoursTextView = findViewById(R.id.confirmed_hours);
        primaryToolbar = findViewById(R.id.primary_toolbar);
        parkButton = findViewById(R.id.qr_park_fab);
        setSupportActionBar(primaryToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        }
        Intent intent = getIntent();
        final int hours = intent.getIntExtra("hours", 0);
        hoursText = "Parking here for " + hours + " hours.";
        hoursTextView.setText(hoursText);
        setQRButtonListener(hours);


    }

    //TODO add the QR library and do its validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(QRActivity.this, "Bluetooth is now turned on", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(QRActivity.this, "The process is canceled", Toast.LENGTH_SHORT).show();
                parkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setQRButtonListener(final int hours) {
        parkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigateToParkedActivity = new Intent(QRActivity.this, ParkedActivity.class);
                navigateToParkedActivity.putExtra("hours", hours);
                startActivity(navigateToParkedActivity);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.e("QR Activity", "onDestroy() called");
        super.onDestroy();
    }
}
