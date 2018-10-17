package com.gvjay.classmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;
import com.gvjay.classmanager.DebugUtils.DebugNotifier;
import com.gvjay.classmanager.Seed.Seeder;

import java.util.Calendar;

import static com.gvjay.classmanager.AttendanceNotificationWorker.CHANNEL_DESC;
import static com.gvjay.classmanager.AttendanceNotificationWorker.CHANNEL_ID;
import static com.gvjay.classmanager.AttendanceNotificationWorker.CHANNEL_NAME;

public class MainActivity extends AppCompatActivity implements ReloadClassData, PageChangeNotify,
        NavigationView.OnNavigationItemSelectedListener{

    private DayAdapter dayAdapter;
    private boolean wasPaused;
    private ViewPager viewPager;
    private int adapterPosition;

    private static ReloadClassData reloadClassData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createNotificationChannel(this);
        reloadClassData = this;

        Log.i("On Create", "Triggered");
        wasPaused = false;
        Calendar calendar = Calendar.getInstance();
        adapterPosition = calendar.get(Calendar.DAY_OF_WEEK);

        //Intent intent = new Intent(this, NotificationService.class);
        //startService(intent);
        WorkerManager.restartAllWorkers();

        viewPager = findViewById(R.id.viewPager);
        dayAdapter = new DayAdapter(getSupportFragmentManager());
        viewPager.setAdapter(dayAdapter);

        DayPageListener pageListener = new DayPageListener(viewPager, this);
        viewPager.addOnPageChangeListener(pageListener);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddClassActivity.class);
                intent.putExtra(AddClassActivity.DAY_NUMBER_KEY, DayAdapter.getRealDayNumber(viewPager.getCurrentItem()));
                startActivity(intent);
            }
        });

        Button testBtn = findViewById(R.id.testBackTaskBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                dbHelper.clearDB();
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                long fromTime = (calendar.get(Calendar.HOUR_OF_DAY)*DateUtils.HOUR_IN_MILLIS)
                        + (calendar.get(Calendar.MINUTE)*DateUtils.MINUTE_IN_MILLIS);
                fromTime += DateUtils.MINUTE_IN_MILLIS;
                long toTime = fromTime + DateUtils.MINUTE_IN_MILLIS;
                ClassObject classObject = new ClassObject("Test", day, fromTime, toTime);
                dbHelper.addClass(classObject);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(getMenuIdToCheck());
    }

    public static void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        DebugNotifier.createNotificationChannel(context);
    }

    @Override
    public void reloadData() {
        dayAdapter = new DayAdapter(getSupportFragmentManager());
        viewPager.setAdapter(dayAdapter);
        viewPager.setCurrentItem(adapterPosition, true);
    }

    public static ReloadClassData getDataReloader() {
        return reloadClassData;
    }

    @Override
    public void notifyPageChanged(int pageNumber) {
        adapterPosition = pageNumber;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_sun:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.menu_mon:
                viewPager.setCurrentItem(2, true);
                break;
            case R.id.menu_tue:
                viewPager.setCurrentItem(3, true);
                break;
            case R.id.menu_wed:
                viewPager.setCurrentItem(4, true);
                break;
            case R.id.menu_thur:
                viewPager.setCurrentItem(5, true);
                break;
            case R.id.menu_fri:
                viewPager.setCurrentItem(6, true);
                break;
            case R.id.menu_sat:
                viewPager.setCurrentItem(7, true);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private int getMenuIdToCheck(){
        int options[] = {R.id.menu_sun, R.id.menu_mon, R.id.menu_tue, R.id.menu_wed, R.id.menu_thur,
        R.id.menu_fri, R.id.menu_sat};
        return options[adapterPosition - 1];
    }
}
