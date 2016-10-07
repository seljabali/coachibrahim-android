package com.codingcamels.coachibrahim.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.codingcamels.coachibrahim.R;

/**
 * Created by habibi on 12/6/14.
 */
public class CalendarViewFragment extends Fragment {
    public static final String TAG = CalendarViewFragment.class.getSimpleName();
    private CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
