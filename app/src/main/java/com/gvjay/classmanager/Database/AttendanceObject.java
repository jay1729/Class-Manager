package com.gvjay.classmanager.Database;

import java.util.Date;

public class AttendanceObject {

    public static String TABLE_NAME = "attendance";

    public static String COLUMN_ID = "id";
    public static String COLUMN_TITLE = "class_title";
    public static String COLUMN_DATE = "date";
    public static String COLUMN_STATUS = "status";

    public static class Choices {
        public static final String POSITIVE = "Present";
        public static final String NEGATIVE = "Absent";
        public static final String NEUTRAL = "Not Counted";
    }

    public static String CREATE_TABLE_COMMAND = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_DATE + " INTEGER, "
            + COLUMN_STATUS + " TEXT"
            + ")";

    public long id;
    public String classTitle;
    public long date;
    public String status;

    public AttendanceObject() {}

    public AttendanceObject(int id, String classTitle, long date, String status){
        this.id = id;
        this.classTitle = classTitle;
        this.date = date;
        this.status = status;
    }

    public AttendanceObject(String classTitle, long date, String status){
        this.classTitle = classTitle;
        this.date = date;
        this.status = status;
    }

    public AttendanceObject(int id, String classTitle, Date date, String status){
        this.id = id;
        this.classTitle = classTitle;
        this.date = date.getTime();
        this.status = status;
    }

    public AttendanceObject(String classTitle, Date date, String status){
        this.classTitle = classTitle;
        this.date = date.getTime();
        this.status = status;
    }
}
