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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QRActivity extends AppCompatActivity {
    private FloatingActionButton parkButton;
    private DatabaseReference mSpecificUserDatabaseReference;
    private int currentPoints;
    private int currentWallet;
    private int timesParked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        TextView hoursTextView;
        String hoursText;
        Toolbar primaryToolbar;
        Intent navToThis = new Intent(QRActivity.this, MainActivity.class);
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
        int hours = intent.getIntExtra("hours", 0);
        hoursText = "Parking here for " + hours + " hours.";
        hoursTextView.setText(hoursText);
        setQRButtonListener(hours);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUID = mFirebaseAuth.getUid();
        DatabaseReference mUsersDatabaseReference = mFirebaseDatabase.getReference("users");
        mSpecificUserDatabaseReference = mUsersDatabaseReference.child(currentUID);
        currentPoints = intent.getIntExtra("points", 0);
        currentWallet = intent.getIntExtra("money", 0);
        timesParked = intent.getIntExtra("timesParked", 0);
    }

    //TODO add the QR library and do its validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
                //get the points count and then increment a 20 to it each park
                //each hours is 2$ worth of parking
                if (currentWallet >= 2) {
                    int enoughMoneyCheck = hours * 2;
                    if (currentWallet >= enoughMoneyCheck) {
                        int newPoints = currentPoints + 10;
                        int newMoney = currentWallet - enoughMoneyCheck;
                        int newTimesParked = timesParked + 1;
                        //TODO: save the date into the database
                        mSpecificUserDatabaseReference.child("points").setValue(newPoints);
                        mSpecificUserDatabaseReference.child("walletMoney").setValue(newMoney);
                        mSpecificUserDatabaseReference.child("timesParked").setValue(newTimesParked);
                        startActivity(navigateToParkedActivity);
                        finish();
                    } else {
                        Toast.makeText(QRActivity.this, "You don't have enough money. Choose less hours", Toast.LENGTH_SHORT).show();
                        Log.i("QR Activity", currentWallet + "");
                    }
                } else {
                    Toast.makeText(QRActivity.this, "You have no money. Charge first!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.e("QR Activity", "onDestroy() called");
        super.onDestroy();
    }

}
