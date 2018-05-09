package com.smartparking.amrbadr12.smartparking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QRActivity extends AppCompatActivity {
    public static final String TAG = "QR Activity";
    private FloatingActionButton parkButton;
    private DatabaseReference mSpecificUserDatabaseReference;
    private int currentPoints;
    private int currentWallet;
    private int timesParked;
    private boolean isScanCorrect;
    private IntentIntegrator qrScan;

    public static Bitmap generateQRCode(String encodedString) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix qr = multiFormatWriter.encode(encodedString, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(qr);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        TextView hoursTextView;
        String hoursText;
        Toolbar primaryToolbar;
        isScanCorrect = false;
        Intent navToThis = new Intent(QRActivity.this, MainActivity.class);
        hoursTextView = findViewById(R.id.confirmed_hours);
        ImageView qrImageView = findViewById(R.id.scan_qr);
        primaryToolbar = findViewById(R.id.primary_toolbar);
        parkButton = findViewById(R.id.qr_park_fab);
        qrScan = new IntentIntegrator(QRActivity.this);
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
        qrImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateQRScan();
            }
        });
    }

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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.i(TAG, "QR code doesn't contain anything");
                isScanCorrect = false;
            } else {
                Toast.makeText(QRActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                isScanCorrect = true;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setQRButtonListener(final int hours) {
        parkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigateToParkedActivity = new Intent(QRActivity.this, ParkedActivity.class);
                navigateToParkedActivity.putExtra("hours", hours);
                //get the points count and then increment a 20 to it each park
                //each hours is 2$ worth of parking
                if (isScanCorrect) {
                    if (currentWallet >= 2) {
                        int enoughMoneyCheck = hours * 2;
                        if (currentWallet >= enoughMoneyCheck) {
                            int newPoints = currentPoints + 10;
                            int newMoney = currentWallet - enoughMoneyCheck;
                            int newTimesParked = timesParked + 1;
                            String date = getNowDate();
                            Log.i(TAG, date);
                            mSpecificUserDatabaseReference.child("points").setValue(newPoints);
                            mSpecificUserDatabaseReference.child("walletMoney").setValue(newMoney);
                            mSpecificUserDatabaseReference.child("timesParked").setValue(newTimesParked);
                            mSpecificUserDatabaseReference.child("lastParkDate").setValue(date);
                            startActivity(navigateToParkedActivity);
                            finish();
                        } else {
                            Toast.makeText(QRActivity.this, "You don't have enough money. Choose less hours", Toast.LENGTH_SHORT).show();
                            Log.i("QR Activity", currentWallet + "");
                        }
                    } else {
                        Toast.makeText(QRActivity.this, "You have no money. Charge first!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QRActivity.this, "You have to scan the QR code first!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String getNowDate() {
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        return simpleDateFormat.format(time);
    }

    public void initiateQRScan() {
        qrScan.initiateScan();
    }

}
