package com.gvjay.classmanager.Database;

import com.gvjay.classmanager.Utils;

public class ClassObject {

    public static String TABLE_NAME = "class";

    public static String COLUMN_ID = "id";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_DAY = "day";
    public static String COLUMN_FROM_TIME = "from_time";
    public static String COLUMN_TO_TIME = "to_time";

    public static String CREATE_TABLE_COMMAND = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_DAY + " INTEGER, "
            + COLUMN_FROM_TIME + " INTEGER, "
            + COLUMN_TO_TIME + " INTEGER, "
            + ")";

    public int id;
    public String title;
    public int day;
    public long fromTime;
    public long toTime;

    public ClassObject(int id, String title, int day, long fromTime, long toTime){
        this.id = id;
        this.title = title;
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public ClassObject(String title, int day, long fromTime, long toTime){
        this.title = title;
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public ClassObject(String title, String day, long fromTime, long toTime){
        this.title = title;
        this.day = Utils.getDayNumber(day);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

}
