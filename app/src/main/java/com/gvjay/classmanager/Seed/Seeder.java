package com.gvjay.classmanager.Seed;

import android.content.Context;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.ArrayList;

public class Seeder {

    private SeedData seedData;
    private DBHelper helper;

    public Seeder(Context context){
        seedData = new SeedData();
        helper = new DBHelper(context);
        helper.clearDB();
    }

    public void seedData(){
        ArrayList<ClassObject> classObjects = seedData.getClassObjects();
        ArrayList<AttendanceObject> attendanceObjects = seedData.getAttendanceObjects();

        int s = classObjects.size();
        for(int i=0;i<s;i++) helper.addClass(classObjects.get(i));
        s = attendanceObjects.size();
        for(int i=0;i<s;i++) helper.addAttendance(attendanceObjects.get(i));
    }
}
