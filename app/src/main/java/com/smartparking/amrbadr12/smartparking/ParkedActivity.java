package com.smartparking.amrbadr12.smartparking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ParkedActivity extends AppCompatActivity {
    private long remainingPeriod;
    private BroadcastReceiver countdownTimerRecieverBroadcast;
    private Intent startingCountDownTimerIntent;
    private TextView hoursTextView;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private FloatingActionButton unparkButton;
    private boolean isServiceStarted;

    public static String convertSecondsToHMmSs(long millseconds) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millseconds),
                TimeUnit.MILLISECONDS.toMinutes(millseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millseconds)),
                TimeUnit.MILLISECONDS.toSeconds(millseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millseconds)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parked);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        Log.e("Parked Activity", "onCreate() is called");
        unparkButton = findViewById(R.id.floatingActionButton);
        startingCountDownTimerIntent = new Intent(this, TimerBroadcastService.class);
        isServiceStarted = mSharedPreferences.getBoolean("STARTED", false);
        if (getIntent() != null && !isServiceStarted) {
                int hours = getIntent().getIntExtra("hours", 0);
                startingCountDownTimerIntent.putExtra("hours", hours);
                if (hours > 0) {//countdown timer is working
                    Log.i("ParkedActivity", "serviceStarted");
                    startService(startingCountDownTimerIntent);
                    mEditor.putBoolean("STARTED", true);
                    mEditor.apply();
                }
        }
        unparkButton = findViewById(R.id.floatingActionButton);
        ProgressBar progressBar = findViewById(R.id.progressbar);
        hoursTextView = findViewById(R.id.hours_textview);
        Toolbar primaryToolbar = findViewById(R.id.primary_toolbar);
        Toolbar cardToolbar = findViewById(R.id.card_view_toolbar);
        setSupportActionBar(primaryToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        }
        unparkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(startingCountDownTimerIntent);
                mEditor.putBoolean("STARTED", false);
                mEditor.apply();
                onBackPressed();
                finish();
            }
        });

        hoursTextView.setText("");
        countdownTimerRecieverBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                remainingPeriod = intent.getLongExtra("countdown", 0);
                String hoursText = convertSecondsToHMmSs(remainingPeriod);
                hoursTextView.setText(hoursText);
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        //this method tells the system to listen to the service broadcast through the action COUNTDOWN_BR
        //and sends its broadcast to those who are interested in such intent.
        if (countdownTimerRecieverBroadcast != null)
            registerReceiver(countdownTimerRecieverBroadcast, new IntentFilter(TimerBroadcastService.COUNTDOWN_BR));
    }


    @Override
    protected void onPause() {
        hoursTextView.setText("");
        super.onPause();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(countdownTimerRecieverBroadcast);
        hoursTextView.setText("");
        super.onStop();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("Parked Activity", "onNewIntent() got called");
    }

    @Override
    protected void onDestroy() {
        countdownTimerRecieverBroadcast = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
