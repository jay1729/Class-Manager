package com.gvjay.classmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.ClassObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "CMDatabase.db";

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

    public long addClass(ClassObject classObject){
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
        while(true){
            output.add(new ClassObject(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                    cursor.getLong(3), cursor.getLong(4)));
            if(cursor.isLast()) break;
        }
        cursor.close();
        return output;
    }
}
