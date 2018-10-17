package com.gvjay.classmanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gvjay.classmanager.Database.AttendanceObject;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinner = findViewById(R.id.as_spinner);
        String[] options = {AttendanceObject.Choices.POSITIVE, AttendanceObject.Choices.NEUTRAL, AttendanceObject.Choices.NEGATIVE};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.day_spinner_item, options);
        spinner.setAdapter(arrayAdapter);

        String currentDefEntry = Utils.getDefaultAttendanceStatus(this);
        int selected = -1;
        for(int i=0;i<3;i++){
            if(currentDefEntry.equals(options[i])) selected = i;
        }
        spinner.setSelection(selected);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_pref_name), MODE_PRIVATE).edit();
                editor.remove(AttendanceObject.DEFAULT_ENTRY_KEY);
                editor.putString(AttendanceObject.DEFAULT_ENTRY_KEY, arrayAdapter.getItem(position));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
