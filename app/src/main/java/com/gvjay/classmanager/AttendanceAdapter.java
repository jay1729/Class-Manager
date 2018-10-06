package com.gvjay.classmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gvjay.classmanager.Database.AttendanceObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceVH> {

    private ArrayList<AttendanceObject> data;
    private Calendar calendar;

    AttendanceAdapter(){
        calendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public AttendanceVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_tile, viewGroup, false);
        return new AttendanceVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceVH attendanceVH, int i) {
        calendar.setTimeInMillis(data.get(i).date);
        attendanceVH.day.setText(Utils.getDayFromNumber(calendar.get(Calendar.DAY_OF_WEEK)-1));
        attendanceVH.date.setText((new Date(data.get(i).date)).toString().substring(0, 10));
        attendanceVH.status.setText(data.get(i).status);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AttendanceVH extends RecyclerView.ViewHolder{

        TextView day;
        TextView date;
        TextView status;

        AttendanceVH(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.at_dayTV);
            date = itemView.findViewById(R.id.at_dateTV);
            status = itemView.findViewById(R.id.at_status);
        }
    }

    public void setData(ArrayList<AttendanceObject> attendanceObjects){
        this.data = attendanceObjects;
        notifyDataSetChanged();
    }
}
