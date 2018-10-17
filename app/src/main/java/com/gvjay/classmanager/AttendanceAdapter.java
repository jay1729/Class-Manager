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
    private int[] visibility;

    AttendanceAdapter(DBHelper dbHelper, ReloadData reloadData){
        calendar = Calendar.getInstance();
        this.dbHelper = dbHelper;
        this.reloadData = reloadData;
    }

    @NonNull
    @Override
    public AttendanceVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_tile, viewGroup, false);
        return new AttendanceVH(view, this, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceVH attendanceVH, int i) {
        if(i >= data.size()){
            setVisibility(attendanceVH.itemView, 1);
            attendanceVH.statusRG.setVisibility(View.INVISIBLE);
            attendanceVH.deleteButton.setVisibility(View.INVISIBLE);
            attendanceVH.itemView.findViewById(R.id.view2).setVisibility(View.INVISIBLE);
            return;
        }
        attendanceVH.itemView.findViewById(R.id.view2).setVisibility(View.VISIBLE);
        calendar.setTimeInMillis(data.get(i).date);
        attendanceVH.day.setText(Utils.getDayFromNumber(calendar.get(Calendar.DAY_OF_WEEK)-1));
        attendanceVH.date.setText((new Date(data.get(i).date)).toString().substring(0, 10));
        attendanceVH.status.setText(data.get(i).status);
        Log.i("Status for "+i, data.get(i).status);
        attendanceVH.statusRG.setOnCheckedChangeListener(null);
        switch (data.get(i).status){
            case AttendanceObject.Choices.POSITIVE:
                attendanceVH.statusRG.check(R.id.at_positive_status);
                break;
            case AttendanceObject.Choices.NEGATIVE:
                attendanceVH.statusRG.check(R.id.at_negative_status);
                break;
            case AttendanceObject.Choices.NEUTRAL:
                attendanceVH.statusRG.check(R.id.at_neutral_status);
                break;
        }
        attendanceVH.statusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ind) {
                switch (ind){
                    case R.id.at_positive_status:
                        changeStatus(attendanceVH.getAdapterPosition(), AttendanceObject.Choices.POSITIVE);
                        break;
                    case R.id.at_negative_status:
                        changeStatus(attendanceVH.getAdapterPosition(), AttendanceObject.Choices.NEGATIVE);
                        break;
                    case R.id.at_neutral_status:
                        changeStatus(attendanceVH.getAdapterPosition(), AttendanceObject.Choices.NEUTRAL);
                        break;
                }
                switchVisibility(attendanceVH.itemView);
            }
        });

        setVisibility(attendanceVH.itemView, visibility[i]);
        attendanceVH.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchVisibility(attendanceVH.itemView);
                visibility[attendanceVH.getAdapterPosition()] = 1-visibility[attendanceVH.getAdapterPosition()];
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    @Override
    public void changeStatus(int position, String newStatus) {
        if(position < 0) return;
        AttendanceObject attendanceObject = this.data.get(position);
        if(attendanceObject.status.equals(newStatus)){
            Log.i("changeStatus", "No change detected");
            return;
        }
        attendanceObject.status = newStatus;
        dbHelper.updateAttendanceRecord(attendanceObject);

        reloadData.loadData();
    }

    @Override
    public void deleteAttendanceRecord(int adapterPosition){
        dbHelper.deleteAttendanceByID(data.get(adapterPosition).id);

        reloadData.loadData();
    }

    public class AttendanceVH extends RecyclerView.ViewHolder{

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

        AttendanceVH(@NonNull final View itemView, final StatusChanger statusChanger, DeleteAttendance deleteAttendance) {
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

            positive.setText(AttendanceObject.Choices.POSITIVE);
            negative.setText(AttendanceObject.Choices.NEGATIVE);
            neutral.setText(AttendanceObject.Choices.NEUTRAL);
            deleteButton = itemView.findViewById(R.id.at_delete_btn);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchVisibility(itemView);
                    deleteAttendanceRecord(getAdapterPosition());
                }
            });

        }
    }

    public void switchVisibility(View view){
        RadioGroup statusRG = view.findViewById(R.id.at_statusRG);
        TextView day = view.findViewById(R.id.at_dayTV);
        TextView date = view.findViewById(R.id.at_dateTV);
        TextView status = view.findViewById(R.id.at_status);
        ImageButton deleteButton = view.findViewById(R.id.at_delete_btn);

        int[] options = {View.VISIBLE, View.INVISIBLE};
        int i = (statusRG.getVisibility() == View.VISIBLE) ? 0 : 1;

        statusRG.setVisibility(options[1-i]);
        deleteButton.setVisibility(options[1-i]);
        day.setVisibility(options[i]);
        date.setVisibility(options[i]);
        status.setVisibility(options[i]);
    }

    void setVisibility(View view, int i){
        RadioGroup statusRG = view.findViewById(R.id.at_statusRG);
        TextView day = view.findViewById(R.id.at_dayTV);
        TextView date = view.findViewById(R.id.at_dateTV);
        TextView status = view.findViewById(R.id.at_status);
        ImageButton deleteButton = view.findViewById(R.id.at_delete_btn);

        int[] options = {View.VISIBLE, View.INVISIBLE};

        statusRG.setVisibility(options[1-i]);
        deleteButton.setVisibility(options[1-i]);
        day.setVisibility(options[i]);
        date.setVisibility(options[i]);
        status.setVisibility(options[i]);
    }

    public void setData(ArrayList<AttendanceObject> attendanceObjects){
        this.data = attendanceObjects;
        int size = attendanceObjects.size();
        visibility = new int[size];
        for(int i=0;i<size;i++) visibility[i] = 0;
        notifyDataSetChanged();
    }

    public interface ReloadData {
        public void loadData();
    }
}
