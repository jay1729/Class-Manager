package com.gvjay.classmanager;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.Calendar;

public class AddClassActivity extends AppCompatActivity {

    public static String DAY_NUMBER_KEY = "day";
    public static String TIME_SLOT_UNAVAILABLE_MSG = "This time slot is already occupied";
    public static String FROM_AFTER_TO_MSG = "From time cannot be after to";
    public static String CLASS_ADDED_MSG = "New Class Added!";

    private int dayNumber;
    private String title;
    private long fromTime;
    private long toTime;
    private Calendar calendar;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        dbHelper = new DBHelper(this);
        calendar = Calendar.getInstance();
        dayNumber = getIntent().getIntExtra(DAY_NUMBER_KEY, -1);
        title = "";
        fromTime = calendar.get(Calendar.HOUR_OF_DAY)*DateUtils.HOUR_IN_MILLIS + calendar.get(Calendar.MINUTE)*DateUtils.MINUTE_IN_MILLIS;
        toTime = fromTime;

        long hour = fromTime / DateUtils.HOUR_IN_MILLIS;
        long minute = (fromTime % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
        Log.i("Time", hour + " " + minute);

        Spinner spinner = findViewById(R.id.aac_day_spinner);
        final ArrayAdapter<String> dayOptions = new ArrayAdapter<>(this, R.layout.day_spinner_item, Consts.daysOfTheWeek);
        spinner.setAdapter(dayOptions);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dayNumber = Utils.getDayNumber(dayOptions.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        EditText titleET = findViewById(R.id.aac_name_et);
        titleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                title = editable.toString();
            }
        });

        final TextView fromTV = findViewById(R.id.aac_from_et);
        fromTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long hour = fromTime / DateUtils.HOUR_IN_MILLIS;
                long minute = (fromTime % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromTime = (selectedHour*DateUtils.HOUR_IN_MILLIS) + (selectedMinute*DateUtils.MINUTE_IN_MILLIS);
                        fromTV.setText((fromTime / DateUtils.HOUR_IN_MILLIS) + ":" + ((fromTime % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS));
                    }
                }, (int) hour, (int) minute, true);
                timePickerDialog.show();
            }
        });
        final TextView toTV = findViewById(R.id.aac_to_et);
        toTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long hour = toTime / DateUtils.HOUR_IN_MILLIS;
                long minute = (toTime % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        toTime = (selectedHour*DateUtils.HOUR_IN_MILLIS) + (selectedMinute*DateUtils.MINUTE_IN_MILLIS);
                        toTV.setText((toTime / DateUtils.HOUR_IN_MILLIS) + ":" + ((toTime % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS));
                    }
                }, (int) hour, (int) minute, true);
                timePickerDialog.show();
            }
        });

        Button okBtn = findViewById(R.id.aac_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewClass();
            }
        });
        Button cancelBtn = findViewById(R.id.aac_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addNewClass(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ClassObject classObject = new ClassObject(title, dayNumber, fromTime, toTime);
        long id = (int) dbHelper.addClass(classObject);
        if(id > 0){
            Toast.makeText(this, CLASS_ADDED_MSG, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if(id == DBHelper.ERROR_TIME_SLOT_UNAVAILABLE){
            builder.setMessage(TIME_SLOT_UNAVAILABLE_MSG);
        }else if(id == DBHelper.ERROR_FROM_AFTER_TO){
            builder.setMessage(FROM_AFTER_TO_MSG);
        }
        builder.show();
    }
}
