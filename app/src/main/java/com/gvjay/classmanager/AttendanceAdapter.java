package com.gvjay.classmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceVH> implements StatusChanger, DeleteAttendance {

    private ArrayList<AttendanceObject> data;
    private Calendar calendar;
    private DBHelper dbHelper;
    private ReloadData reloadData;

    AttendanceAdapter(DBHelper dbHelper, ReloadData reloadData){
        calendar = Calendar.getInstance();
        this.dbHelper = dbHelper;
        this.reloadData = reloadData;
    }

    @NonNull
    @Override
    public AttendanceVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_tile, viewGroup, false);
        return new AttendanceVH(view, this, this, data.get(i).status);
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

    @Override
    public void changeStatus(int position, String newStatus) {
        AttendanceObject attendanceObject = this.data.get(position);
        attendanceObject.status = newStatus;
        dbHelper.updateAttendanceRecord(attendanceObject);

        reloadData.loadData();
    }

    @Override
    public void deleteAttendanceRecord(int adapterPosition){
        dbHelper.deleteAttendanceByID(data.get(adapterPosition).id);

        reloadData.loadData();
    }

    public static class AttendanceVH extends RecyclerView.ViewHolder{

        TextView day;
        TextView date;
        TextView status;
        ImageButton deleteButton;

        RadioGroup statusRG;
        RadioButton positive;
        RadioButton negative;
        RadioButton neutral;
        private StatusChanger statusChanger;
        DeleteAttendance deleteAttendance;

        AttendanceVH(@NonNull View itemView, final StatusChanger statusChanger, DeleteAttendance deleteAttendance, String currentStatus) {
            super(itemView);
            this.statusChanger = statusChanger;
            this.deleteAttendance = deleteAttendance;

            day = itemView.findViewById(R.id.at_dayTV);
            date = itemView.findViewById(R.id.at_dateTV);
            status = itemView.findViewById(R.id.at_status);

            statusRG = itemView.findViewById(R.id.at_statusRG);
            positive = itemView.findViewById(R.id.at_positive_status);
            negative = itemView.findViewById(R.id.at_negative_status);
            neutral = itemView.findViewById(R.id.at_neutral_status);

            Log.i("Current State is", currentStatus);
            switch(currentStatus){
                case AttendanceObject.Choices.POSITIVE:
                    positive.setChecked(true);
                    break;
                case AttendanceObject.Choices.NEGATIVE:
                    negative.setChecked(true);
                    break;
                case AttendanceObject.Choices.NEUTRAL:
                    neutral.setChecked(true);
                    break;
            }

            positive.setText(AttendanceObject.Choices.POSITIVE);
            negative.setText(AttendanceObject.Choices.NEGATIVE);
            neutral.setText(AttendanceObject.Choices.NEUTRAL);
            deleteButton = itemView.findViewById(R.id.at_delete_btn);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AttendanceVH.this.deleteAttendance.deleteAttendanceRecord(getAdapterPosition());
                }
            });

            statusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i){
                        case R.id.at_positive_status:
                            statusChanger.changeStatus(getAdapterPosition(), AttendanceObject.Choices.POSITIVE);
                            break;
                        case R.id.at_negative_status:
                            statusChanger.changeStatus(getAdapterPosition(), AttendanceObject.Choices.NEGATIVE);
                            break;
                        case R.id.at_neutral_status:
                            statusChanger.changeStatus(getAdapterPosition(), AttendanceObject.Choices.NEUTRAL);
                            break;
                    }
                    switchVisibility();
                }
            });

            statusRG.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    switchVisibility();
                    return false;
                }
            });
        }

        void switchVisibility(){
            int[] options = {View.VISIBLE, View.INVISIBLE};
            int i = (statusRG.getVisibility() == View.VISIBLE) ? 0 : 1;

            statusRG.setVisibility(options[1-i]);
            deleteButton.setVisibility(options[1-i]);
            day.setVisibility(options[i]);
            date.setVisibility(options[i]);
            status.setVisibility(options[i]);
        }
    }

    public void setData(ArrayList<AttendanceObject> attendanceObjects){
        this.data = attendanceObjects;
        notifyDataSetChanged();
    }

    public interface ReloadData {
        public void loadData();
    }
}
