package com.smartparking.amrbadr12.smartparking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatsFragment extends android.support.v4.app.Fragment {
    private TextView lastVisitTextView;
    private TextView parkedCountTextView;
    private TextView pointsCountTextView;
    private stats statsObject;

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
//        lastVisitTextView.setText(statsObject.getLastVisitedDate());
//        parkedCountTextView.setText(statsObject.getParkCount());
//        pointsCountTextView.setText(statsObject.getPointsCount());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof stats) {
            statsObject = (stats) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement stats interface in profile activity.");
        }
    }

    public interface stats {
        int getParkCount();

        String getLastVisitedDate();

        int getPointsCount();
    }
}
