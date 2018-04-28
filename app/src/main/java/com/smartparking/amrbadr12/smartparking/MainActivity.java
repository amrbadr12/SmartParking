package com.smartparking.amrbadr12.smartparking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private SharedPreferences.Editor edit;
    private boolean secondTime;
    private ImageView circleView;
    private TextView hintText;
    private FloatingActionButton parkButton;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView pointsCount = findViewById(R.id.points_count);
        hintText = findViewById(R.id.hint);
        Toolbar primaryToolbar = findViewById(R.id.primary_toolbar);
        Toolbar cardviewToolbar = findViewById(R.id.card_view_toolbar);
        circleView = findViewById(R.id.imageView);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        edit = mSharedPreferences.edit();
        secondTime = mSharedPreferences.getBoolean("STARTED", false);
        Log.e("Main Activity", "onCreate() is called");
        parkButton = findViewById(R.id.floatingActionButton);
        setSupportActionBar(primaryToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        parkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showParkDialog();
            }
        });
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.menu_profile:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    case R.id.menu_faq:
                        startActivity(new Intent(MainActivity.this, FAQActivity.class));
                        break;
                    case R.id.menu_settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //the service is now running and the user clicked the back button

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout != null)
                    mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    public void showParkDialog() {
        View dialogLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null);
        Button decreaseHours = dialogLayout.findViewById(R.id.decrease_hours);
        Button increaseHours = dialogLayout.findViewById(R.id.increase_hours);
        final TextView hoursText = dialogLayout.findViewById(R.id.hours_count_text);
        increaseHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hours = Integer.parseInt(hoursText.getText().toString());
                if (hours < 24) {
                    hours++;
                    hoursText.setText(hours + "");
                }
            }
        });
        decreaseHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hours = Integer.parseInt(hoursText.getText().toString());
                if (hours > 0) {
                    hours--;
                    hoursText.setText(hours + "");
                }

            }
        });
        new AlertDialog.Builder(MainActivity.this).setView(dialogLayout).setMessage("How many hours would you like to park?")
                .setTitle("Park Duration").setCancelable(true).setIcon(R.drawable.ic_hours)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Park", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hours = Integer.parseInt(hoursText.getText().toString());
                if (hours > 0) {
                    Intent startQrActivity = new Intent(MainActivity.this, QRActivity.class);
                    startQrActivity.putExtra("hours", hours);
                    secondTime = true;
                    startActivity(startQrActivity);
                } else {
                    Toast.makeText(MainActivity.this, "Hours can't be zero.", Toast.LENGTH_SHORT).show();
                }
            }
        }).create().show();
    }

    /*
    Main Activity<<QR Activity<< Parked Activity
    Main Activity<< Parked Activiry (Service is running in Parked Activity)
    if back button is pressed you get the same instance of Main activity but with different prefrences
    Main Activity<< Parked Activity<< Parked Activity
     */

    @Override
    protected void onResume() {
        super.onResume();
        secondTime = mSharedPreferences.getBoolean("STARTED", false);
        if (secondTime) {
            Log.i("Main Activity", "Second Time");
            hintText.setVisibility(View.VISIBLE);
            circleView.setBackgroundResource(R.drawable.circle_drawable_green);
            parkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ParkedActivity.class);
                    intent.putExtra("resume_count", true);
                    startActivity(intent);
                }
            });
        } else {
            Log.i("Main Activity", "First Time");
            hintText.setVisibility(View.INVISIBLE);
            circleView.setBackgroundResource(R.drawable.circle_drawable);
            parkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showParkDialog();
                }
            });
        }
    }
}
