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

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 212;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private SharedPreferences.Editor edit;
    private boolean secondTime;
    private ImageView circleView;
    private TextView hintText;
    private TextView usernameTextView;
    private TextView pointsCountTextView;
    private TextView lastVisitedDateTextView;
    private TextView freeSlotsTextView;
    private FloatingActionButton parkButton;
    private String uniqueID;
    private SharedPreferences mSharedPreferences;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mSpecificUserDatabaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mFirebaseAuth;
    private ValueEventListener mValueEventListener;
    private String username;
    private String currentUID;
    private int currentMoney;
    private int timesParked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mFirebaseDatabase == null) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference("users");
        usernameTextView = findViewById(R.id.username);
        pointsCountTextView = findViewById(R.id.points_count);
        lastVisitedDateTextView = findViewById(R.id.last_visited_date);
        freeSlotsTextView = findViewById(R.id.free_slots_count);
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
                    case R.id.menu_log_out:
                        mDrawerLayout.closeDrawers();
                        AuthUI.getInstance().signOut(MainActivity.this);
                    default:
                        break;
                }
                return true;
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                //the user just logged in
                if (currentUser != null) {
                    Log.i(TAG, "user is signed in");
                    currentUID = currentUser.getUid();
                    username = currentUser.getDisplayName();
                    addNewUser();
                    attachDatabaseReadListener();
                }
                //user is not logged in, show login screen
                else {
                    startFirebaseAuthUI();
                    Log.i(TAG, "user is not signed in");
                }
            }
        };

    }

    public boolean addNewUser() {
        //adds new user to the database and checks if there's a duplicite
        //user is added for the first time
        if (mUsersDatabaseReference.child(currentUID) == null) {
            UserData user = new UserData(username);
            mUsersDatabaseReference.child(currentUID).setValue(user);
            return true;
        }
        mSpecificUserDatabaseReference = mUsersDatabaseReference.child(currentUID);
        Log.i(TAG, "user already exists");
        return false;
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


    public void showParkDialog() {
        View dialogLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null);
        Button decreaseHours = dialogLayout.findViewById(R.id.decrease_hours);
        Button increaseHours = dialogLayout.findViewById(R.id.increase_hours);
        final TextView hoursText = dialogLayout.findViewById(R.id.hours_count_text);
        increaseHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hours = Integer.parseInt(hoursText.getText().toString()); // 0
                if (hours < 24) {
                    hours++; //1
                    hoursText.setText(hours + "");
                }
            }
        });
        decreaseHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hours = Integer.parseInt(hoursText.getText().toString()); //1
                if (hours > 0) {
                    hours--;//0
                    hoursText.setText(hours + "");
                }

            }
        });
        new AlertDialog.Builder(MainActivity.this).setView(dialogLayout).setMessage(getResources().getString(R.string.dialog_display_message))
                .setTitle("Park Duration").setCancelable(true).setIcon(R.drawable.ic_hours)
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton(getResources().getString(R.string.park_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hours = Integer.parseInt(hoursText.getText().toString());
                if (hours > 0) {
                    Intent startQrActivity = new Intent(MainActivity.this, QRActivity.class);
                    startQrActivity.putExtra("hours", hours);
                    startQrActivity.putExtra("points", Integer.parseInt(pointsCountTextView.getText().toString()));
                    startQrActivity.putExtra("money", currentMoney);
                    startQrActivity.putExtra("timesParked", timesParked);
                    secondTime = true;
                    startActivity(startQrActivity);
                } else {
                    Toast.makeText(MainActivity.this, R.string.hours_not_zero_label, Toast.LENGTH_SHORT).show();
                }
            }
        }).create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //auth state listener for login changes
        attachAuthStateListener();
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
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //do nothing
                }
            });
            parkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showParkDialog();
                }
            });
        }
    }

    public void startFirebaseAuthUI() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        startActivityForResult(AuthUI.getInstance().
                        createSignInIntentBuilder().setIsSmartLockEnabled(false).
                        setAvailableProviders(providers).setTheme(R.style.AuthUITheme).build()
                , RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "user is signed in");
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private void attachDatabaseReadListener() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                timesParked = dataSnapshot.child("timesParked").getValue(Integer.class);
                Log.i(TAG, timesParked + "");
                currentMoney = dataSnapshot.child("walletMoney").getValue(Integer.class);
                int points = dataSnapshot.child("points").getValue(Integer.class);
                Log.i(TAG, "points+" + points);
                pointsCountTextView.setText(Integer.toString(points));
                String userName = dataSnapshot.child("userName").getValue(String.class);
                usernameTextView.setText(userName);
                String lastParkDate = dataSnapshot.child("lastParkDate").getValue(String.class);
                if (lastParkDate.equals("")) {
                    lastVisitedDateTextView.setText("First time");
                } else {
                    lastVisitedDateTextView.setText(lastParkDate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (mSpecificUserDatabaseReference != null) {
            mSpecificUserDatabaseReference.addValueEventListener(mValueEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mValueEventListener != null && mSpecificUserDatabaseReference != null) {
            mSpecificUserDatabaseReference.removeEventListener(mValueEventListener);
        }
    }

    private void attachAuthStateListener() {
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    private void detachAuthStateListener() {
        mFirebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        detachDatabaseReadListener();
        detachAuthStateListener();
    }
}
