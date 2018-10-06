package com.gvjay.classmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gvjay.classmanager.Database.AttendanceObject;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceVH> {

    private ArrayList<AttendanceObject> data;

    @NonNull
    @Override
    public AttendanceVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceVH attendanceVH, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class AttendanceVH extends RecyclerView.ViewHolder{

        public AttendanceVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setData(ArrayList<AttendanceObject> attendanceObjects){
        this.data = attendanceObjects;
        notifyDataSetChanged();
    }
}
