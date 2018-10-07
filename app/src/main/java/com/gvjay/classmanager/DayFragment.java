package com.gvjay.classmanager;

import android.content.Context;
import android.content.Intent;
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
        int s = classes.size();
        double[] attendancePC = new double[s];
        for(int i=0;i<s;i++){
            attendancePC[i] = Utils.calculateAttendance(dbHelper.getAttendanceByTitle(classes.get(i).title));
        }

        View view = inflater.inflate(R.layout.fragment_day, container, false);

        dayTextView = view.findViewById(R.id.dayTextView);
        dayTextView.setText(Utils.getDayFromNumber(day));

        ClassAdapter adapter = new ClassAdapter(classes, attendancePC);
        RecyclerView recyclerView = view.findViewById(R.id.classRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public class ClassAdapter extends RecyclerView.Adapter<ClassViewHolder>{

        private ArrayList<ClassObject> data;
        private double[] attendancePC;

        ClassAdapter(ArrayList<ClassObject> data, double[] attendancePC){
            this.data = data;
            this.attendancePC = attendancePC;
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
            String attendance;
            if(this.attendancePC[i] < 0){
                attendance = "NA";
            }else{
                attendance = String.valueOf(attendancePC[i]);
                attendance = attendance.substring(0, (4 < attendance.length()) ? 4 : attendance.length());
                attendance += "%";
            }
            classViewHolder.attendance.setText(attendance);
            classViewHolder.classTiming.setText(Utils.getTiming(this.data.get(i).fromTime, this.data.get(i).toTime));

            classViewHolder.itemView.setOnClickListener(new ClassClickListener(this.data.get(i).title, context));
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

    public class ClassClickListener implements View.OnClickListener{

        private String classTitle;
        private Context context;

        ClassClickListener(String classTitle, Context context){
            this.classTitle = classTitle;
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ClassActivity.class);
            intent.putExtra(ClassActivity.CLASS_TITLE_KEY, classTitle);

            startActivity(intent);
        }
    }
}
