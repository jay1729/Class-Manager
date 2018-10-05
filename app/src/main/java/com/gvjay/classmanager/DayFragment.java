package com.gvjay.classmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;

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
        DBHelper dbHelper = new DBHelper(context);
        classes = dbHelper.getClassesOnDay(day);


        View view = inflater.inflate(R.layout.fragment_day, container, false);

        dayTextView = view.findViewById(R.id.dayTextView);
        dayTextView.setText(Utils.getDayFromNumber(day));

        ClassAdapter adapter = new ClassAdapter(classes);
        RecyclerView recyclerView = view.findViewById(R.id.classRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public class ClassAdapter extends RecyclerView.Adapter<ClassViewHolder>{

        private ArrayList<ClassObject> data;

        ClassAdapter(ArrayList<ClassObject> data){
            this.data = data;
        }

        @NonNull
        @Override
        public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_tile, viewGroup, false);

            return new ClassViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ClassViewHolder classViewHolder, int i) {
            classViewHolder.className.setText(this.data.get(i).title);
            Log.i("The wwwww", String.valueOf(data.size()));
        }

        @Override
        public int getItemCount() {
            return this.data.size();
        }
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder{

        public TextView className;
        public TextView classTiming;
        public TextView attendance;

        ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            classTiming = itemView.findViewById(R.id.classTiming);
            attendance = itemView.findViewById(R.id.attendancePC);
        }
    }
}
