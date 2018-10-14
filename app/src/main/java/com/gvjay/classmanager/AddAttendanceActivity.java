package com.gvjay.classmanager;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.Calendar;
import java.util.Date;

public class AddAttendanceActivity extends AppCompatActivity {

    private String classTitle;
    public static String CLASS_TITLE_KEY = "Class Title";
    private long date;
    private String status;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);

        classTitle = getIntent().getStringExtra(CLASS_TITLE_KEY);
        status = AttendanceObject.Choices.POSITIVE;

        TextView title = findViewById(R.id.aaa_class_title);
        title.setText(classTitle);

        final TextView dateView = findViewById(R.id.aaa_date);
        final Calendar calendar = Calendar.getInstance();
        date = calendar.getTimeInMillis();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateView.setText(day + "-" + (month+1) + "-" + (year%100));
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearValue, int monthOfYear, int dayOfMonth) {
                        year = yearValue;
                        month = monthOfYear;
                        day = dayOfMonth;
                        calendar.set(yearValue, monthOfYear, dayOfMonth);
                        date = calendar.getTimeInMillis();
                        dateView.setText(day + "-" + (month+1) + "-" + (year%100));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        RadioButton positiveRB = findViewById(R.id.aaa_positive_rb);
        positiveRB.setText(AttendanceObject.Choices.POSITIVE);
        RadioButton negativeRB = findViewById(R.id.aaa_negative_rb);
        negativeRB.setText(AttendanceObject.Choices.NEGATIVE);
        RadioButton neutralRB = findViewById(R.id.aaa_neutral_rb);
        neutralRB.setText(AttendanceObject.Choices.NEUTRAL);

        RadioGroup radioGroup = findViewById(R.id.aaa_rg);
        radioGroup.check(R.id.aaa_positive_rb);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.aaa_positive_rb:
                        status = AttendanceObject.Choices.POSITIVE;
                        break;
                    case R.id.aaa_negative_rb:
                        status = AttendanceObject.Choices.NEGATIVE;
                        break;
                    case R.id.aaa_neutral_rb:
                        status = AttendanceObject.Choices.NEUTRAL;
                        break;
                }
            }
        });

        Button okBtn = findViewById(R.id.aaa_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAttendanceRecord();
                finish();
            }
        });
        Button cancelBtn = findViewById(R.id.aaa_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addAttendanceRecord(){
        AttendanceObject attendanceObject = new AttendanceObject(classTitle, date, status);
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addAttendance(attendanceObject);
    }
}
