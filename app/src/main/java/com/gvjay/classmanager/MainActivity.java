package com.gvjay.classmanager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gvjay.classmanager.Seed.Seeder;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DayAdapter dayAdapter;
    private boolean wasPaused;
    private ViewPager viewPager;
    private int adapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("On Create", "Triggered");
        wasPaused = false;
        Calendar calendar = Calendar.getInstance();
        adapterPosition = calendar.get(Calendar.DAY_OF_WEEK);

        viewPager = findViewById(R.id.viewPager);
        dayAdapter = new DayAdapter(getSupportFragmentManager());
        viewPager.setAdapter(dayAdapter);

        DayPageListener pageListener = new DayPageListener(viewPager);
        viewPager.addOnPageChangeListener(pageListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasPaused = true;
        adapterPosition = viewPager.getCurrentItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("On Resume", "Triggered");
        if(wasPaused){
            dayAdapter = new DayAdapter(getSupportFragmentManager());
            viewPager.setAdapter(dayAdapter);
        }
        wasPaused = false;
        viewPager.setCurrentItem(adapterPosition, true);
    }
}
