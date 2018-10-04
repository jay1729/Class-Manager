package com.gvjay.classmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gvjay.classmanager.Database.ClassObject;

import java.util.ArrayList;

public class DayFragment extends Fragment {

    public static String DAY_KEY = "day";

    private int day;
    private ArrayList<ClassObject> classes;
    private Context context;
    private TextView dayTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        context = getActivity();
        day = args.getInt(DAY_KEY);

        View view = inflater.inflate(R.layout.fragment_day, container, false);

        dayTextView = view.findViewById(R.id.dayTextView);
        dayTextView.setText(Utils.getDayFromNumber(day));

        return view;
    }
}
