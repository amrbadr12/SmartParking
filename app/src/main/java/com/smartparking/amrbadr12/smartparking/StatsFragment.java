package com.smartparking.amrbadr12.smartparking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatsFragment extends android.support.v4.app.Fragment {
    private TextView lastVisitTextView;
    private TextView parkedCountTextView;
    private TextView pointsCountTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mSpecificUserDatabaseReference;
    private String currentUID;
    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stats_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lastVisitTextView = view.findViewById(R.id.last_visit_date_textview);
        parkedCountTextView = view.findViewById(R.id.times_parked_count_textview);
        pointsCountTextView = view.findViewById(R.id.points_textview);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUID = mFirebaseAuth.getUid();
        mUsersDatabaseReference = mFirebaseDatabase.getReference("users");
        mSpecificUserDatabaseReference = mUsersDatabaseReference.child(currentUID);
    }

    public void attachDatabaseReadListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lastParkDate = dataSnapshot.child("lastParkDate").getValue(String.class);
                int timesParked = dataSnapshot.child("timesParked").getValue(Integer.class);
                int points = dataSnapshot.child("points").getValue(Integer.class);
                lastVisitTextView.setText(lastParkDate);
                if (lastParkDate.equals("")) {
                    parkedCountTextView.setText("First time");
                }
                parkedCountTextView.setText(timesParked + "");
                pointsCountTextView.setText(points + "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mSpecificUserDatabaseReference.addValueEventListener(valueEventListener);
    }

    public void detachDatabaseReadListener() {
        mSpecificUserDatabaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        detachDatabaseReadListener();
    }

}
