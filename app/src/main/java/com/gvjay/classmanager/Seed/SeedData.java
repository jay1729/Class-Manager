package com.gvjay.classmanager.Seed;

import android.text.format.DateUtils;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.ClassObject;

import java.util.ArrayList;
import java.util.Date;

public class SeedData {
    private ArrayList<ClassObject> CLASS_OBJECTS = new ArrayList<>();
    private ArrayList<AttendanceObject> ATTENDANCE_OBJECTS = new ArrayList<>();

    public SeedData(){
        CLASS_OBJECTS.add(new ClassObject("Class 1", 1, 10*DateUtils.HOUR_IN_MILLIS, 12*DateUtils.HOUR_IN_MILLIS));
        CLASS_OBJECTS.add(new ClassObject("Class 1", 2, 14*DateUtils.HOUR_IN_MILLIS, 15*DateUtils.HOUR_IN_MILLIS));
        CLASS_OBJECTS.add(new ClassObject("Class 1", 5, 16*DateUtils.HOUR_IN_MILLIS, 17*DateUtils.HOUR_IN_MILLIS));

        CLASS_OBJECTS.add(new ClassObject("Class 2", 1, 12*DateUtils.HOUR_IN_MILLIS, 13*DateUtils.HOUR_IN_MILLIS));
        CLASS_OBJECTS.add(new ClassObject("Class 2", 2, 12*DateUtils.HOUR_IN_MILLIS, 13*DateUtils.HOUR_IN_MILLIS));
        CLASS_OBJECTS.add(new ClassObject("Class 2", 4, 9*DateUtils.HOUR_IN_MILLIS, 10*DateUtils.HOUR_IN_MILLIS));


        ATTENDANCE_OBJECTS.add(new AttendanceObject("Class 1", new Date(2018,1,1), AttendanceObject.Choices.POSITIVE));
        ATTENDANCE_OBJECTS.add(new AttendanceObject("Class 1", new Date(2018,1,3), AttendanceObject.Choices.POSITIVE));
        ATTENDANCE_OBJECTS.add(new AttendanceObject("Class 1", new Date(2018,1,5), AttendanceObject.Choices.NEGATIVE));

        ATTENDANCE_OBJECTS.add(new AttendanceObject("Class 2", new Date(2018,1,1), AttendanceObject.Choices.POSITIVE));
        ATTENDANCE_OBJECTS.add(new AttendanceObject("Class 2", new Date(2018,1,2), AttendanceObject.Choices.NEGATIVE));
        ATTENDANCE_OBJECTS.add(new AttendanceObject("Class 2", new Date(2018,1,3), AttendanceObject.Choices.NEUTRAL));
    }

    public ArrayList<AttendanceObject> getAttendanceObjects() {
        return ATTENDANCE_OBJECTS;
    }

    public ArrayList<ClassObject> getClassObjects() {
        return CLASS_OBJECTS;
    }
}
