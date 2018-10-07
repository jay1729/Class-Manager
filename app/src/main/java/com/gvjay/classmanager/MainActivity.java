package com.gvjay.classmanager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gvjay.classmanager.Seed.Seeder;

public class MainActivity extends AppCompatActivity {

    private DayAdapter dayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Seeder seeder = new Seeder(this);
        //seeder.seedData();

        ViewPager viewPager = findViewById(R.id.viewPager);
        dayAdapter = new DayAdapter(getSupportFragmentManager());
        viewPager.setAdapter(dayAdapter);

        DayPageListener pageListener = new DayPageListener(viewPager);
        viewPager.addOnPageChangeListener(pageListener);
    }
}
