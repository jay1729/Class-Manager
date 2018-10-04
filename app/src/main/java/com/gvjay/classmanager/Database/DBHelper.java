package com.gvjay.classmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.ClassObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "CMDatabase.db";

    public static long ERROR_TIME_SLOT_UNAVAILABLE = -1;

    private Context context;

    public DBHelper(Context context){
        super(context, DATABASE_NAME , null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ClassObject.CREATE_TABLE_COMMAND);
        sqLiteDatabase.execSQL(AttendanceObject.CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClassObject.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AttendanceObject.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private long isClassAllowed(ClassObject classObject){
        ArrayList<ClassObject> clArrayList = getClassesOnDay(classObject.day);
        int size = clArrayList.size();
        for(int i=0;i<size;i++){
            if((clArrayList.get(i).fromTime < classObject.fromTime) && (classObject.fromTime < clArrayList.get(i).toTime)) return ERROR_TIME_SLOT_UNAVAILABLE;
            if((clArrayList.get(i).fromTime < classObject.toTime) && (classObject.toTime < clArrayList.get(i).toTime)) return ERROR_TIME_SLOT_UNAVAILABLE;
            if((classObject.fromTime <= clArrayList.get(i).fromTime) && (clArrayList.get(i).toTime <= classObject.toTime)) return ERROR_TIME_SLOT_UNAVAILABLE;
        }
        return 1;
    }

    public long addClass(ClassObject classObject){

        long errorCode = isClassAllowed(classObject);
        if(errorCode < 0) return errorCode;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ClassObject.COLUMN_TITLE, classObject.title);
        cv.put(ClassObject.COLUMN_DAY, classObject.day);
        cv.put(ClassObject.COLUMN_FROM_TIME, classObject.fromTime);
        cv.put(ClassObject.COLUMN_TO_TIME, classObject.toTime);

        return db.insert(ClassObject.TABLE_NAME, null, cv);
    }

    public ArrayList<ClassObject> getClassesOnDay(int day){
        ArrayList<ClassObject> output = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ClassObject.TABLE_NAME, null, ClassObject.COLUMN_DAY+"=?",
                new String[]{String.valueOf(day)}, null, null, ClassObject.COLUMN_FROM_TIME);
        cursor.moveToFirst();
        try {
            while(true){
                output.add(new ClassObject(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                        cursor.getLong(3), cursor.getLong(4)));
                if(cursor.isLast()) break;
                cursor.moveToNext();
            }
        } catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        cursor.close();
        return output;
    }

    public long addAttendance(AttendanceObject attendanceObject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AttendanceObject.COLUMN_TITLE, attendanceObject.classTitle);
        cv.put(AttendanceObject.COLUMN_DATE, attendanceObject.date);
        cv.put(AttendanceObject.COLUMN_STATUS, attendanceObject.status);

        return db.insert(AttendanceObject.TABLE_NAME, null, cv);
    }

    public ArrayList<AttendanceObject> getAttendanceByTitle(String classTitle){
        ArrayList<AttendanceObject> output = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(AttendanceObject.TABLE_NAME, null, AttendanceObject.COLUMN_TITLE+"=?",
                new String[]{classTitle}, null, null, '-'+AttendanceObject.COLUMN_DATE);
        cursor.moveToFirst();
        try{
            while (true){
                output.add(new AttendanceObject(cursor.getInt(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3)));
                if(cursor.isLast()) break;
                cursor.moveToNext();
            }
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        cursor.close();
        return output;
    }

    public long updateAttendanceRecord(AttendanceObject attendanceObject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AttendanceObject.COLUMN_TITLE, attendanceObject.classTitle);
        cv.put(AttendanceObject.COLUMN_DATE, attendanceObject.date);
        cv.put(AttendanceObject.COLUMN_STATUS, attendanceObject.status);

        return db.update(AttendanceObject.TABLE_NAME, cv, AttendanceObject.COLUMN_ID+"=?",
                new String[]{String.valueOf(attendanceObject.id)});
    }

    public void clearDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ClassObject.TABLE_NAME, null, null);
        db.delete(AttendanceObject.TABLE_NAME, null, null);
    }
}
