package com.gvjay.classmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity implements AttendanceAdapter.ReloadData{

    private DBHelper dbHelper;
    private String classTitle;
    private ArrayList<AttendanceObject> attendanceObjects;
    private AttendanceAdapter adapter;
    private TextView attendancePC;
    private boolean wasPaused;

    public static String CLASS_TITLE_KEY = "Class Title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        wasPaused = false;
        dbHelper = new DBHelper(this);
        adapter = new AttendanceAdapter(dbHelper, this);
        RecyclerView recyclerView = findViewById(R.id.attendanceRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        classTitle = getIntent().getStringExtra(CLASS_TITLE_KEY);
        TextView classTitleTV = findViewById(R.id.classTitle);
        classTitleTV.setText(classTitle);
        attendancePC = findViewById(R.id.ac_attendance);
        FloatingActionButton addAttendanceBtn = findViewById(R.id.ac_fab);
        addAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassActivity.this, AddAttendanceActivity.class);
                intent.putExtra(AddAttendanceActivity.CLASS_TITLE_KEY, classTitle);

                startActivity(intent);
            }
        });
        loadData();
    }

    public void loadData(){
        attendanceObjects = dbHelper.getAttendanceByTitle(classTitle);
        adapter.setData(attendanceObjects);
        attendancePC.setText(Utils.getAttendanceString(attendanceObjects));
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasPaused) loadData();
        wasPaused = false;
    }
}
