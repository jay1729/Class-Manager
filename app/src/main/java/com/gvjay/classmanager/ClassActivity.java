package com.gvjay.classmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private String classTitle;
    private ArrayList<AttendanceObject> attendanceObjects;
    private AttendanceAdapter adapter;
    private TextView attendancePC;

    public static String CLASS_TITLE_KEY = "Class Title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        dbHelper = new DBHelper(this);
        adapter = new AttendanceAdapter();
        RecyclerView recyclerView = findViewById(R.id.attendanceRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        classTitle = getIntent().getStringExtra(CLASS_TITLE_KEY);
        TextView classTitleTV = findViewById(R.id.classTitle);
        classTitleTV.setText(classTitle);
        attendancePC = findViewById(R.id.ac_attendance);
        loadData();
    }

    public void loadData(){
        attendanceObjects = dbHelper.getAttendanceByTitle(classTitle);
        adapter.setData(attendanceObjects);
        attendancePC.setText(String.valueOf(Utils.calculateAttendance(attendanceObjects)).substring(0, 4));
    }
}
