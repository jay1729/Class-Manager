package com.gvjay.classmanager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BackTask extends AsyncTask<Void, Void, Void>{

    private static BackTask mTask;
    private boolean stopTask;
    private DBHelper dbHelper;
    private ArrayList<ArrayList<ClassObject>> classObjects;
    private int day = 0;
    private boolean[] attendance;
    private boolean[] notification;
    private AttendanceObject[] attendanceObjectsForDay;

    private BackTask() {
        classObjects = new ArrayList<ArrayList<ClassObject>>();
        for(int i=0;i<7;i++){
            classObjects.add(new ArrayList<ClassObject>());
        }
        stopTask = false;
        Log.i("Constructor for", "BackTask");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true){
            SystemClock.sleep(5000);
            Log.i("BackTask", "Ping");
            int newDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            processDay(newDay);
            long time = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*DateUtils.HOUR_IN_MILLIS) +
                    (Calendar.getInstance().get(Calendar.MINUTE)*DateUtils.MINUTE_IN_MILLIS);
            time = time % DateUtils.DAY_IN_MILLIS;
            int size = classObjects.get(day).size();
            for(int i=0;i<size;i++){
                if(Math.abs(time - classObjects.get(day).get(i).fromTime) < DateUtils.MINUTE_IN_MILLIS){
                    if(attendance[i]) continue;
                    attendanceObjectsForDay[i] = addAttendance(classObjects.get(day).get(i));
                    attendance[i] = true;
                }
                if(Math.abs(time - classObjects.get(day).get(i).toTime) < DateUtils.MINUTE_IN_MILLIS){
                    if(notification[i]) continue;
                    sendNotification(attendanceObjectsForDay[i]);
                    notification[i] = true;
                }
            }
            if(stopTask) break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        stopTask = false;
        mTask = null;
    }

    public static void executeTask(Context context){
        if(mTask != null) return;
        mTask = new BackTask();
        mTask.dbHelper = new DBHelper(context);
        syncWithDB();
        mTask.execute();
    }

    public static void stopTask(){
        mTask.stopTask = true;
    }

    public static void syncWithDB(){
        for(int i=0;i<7;i++){
            ArrayList<ClassObject> classObjects = mTask.dbHelper.getClassesOnDay(i);
            Log.i("DBUpdate", classObjects.size()+ " " + i);
            mTask.classObjects.get(i).addAll(classObjects);
        }
        Log.i("DBUpdate", "Synced!!");
        mTask.day = -1;
        mTask.processDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
    }

    private void processDay(int newDay){
        if(newDay == day) return;
        int size = classObjects.get(newDay).size();
        attendance = new boolean[size];
        notification = new boolean[size];
        attendanceObjectsForDay = new AttendanceObject[size];
        for(int i=0;i<size;i++){
            attendance[i] = false;
            notification[i] = false;
        }
        day = newDay;
    }

    private AttendanceObject addAttendance(ClassObject classObject){
        AttendanceObject attendanceObject = new AttendanceObject(classObject.title, Calendar.getInstance().getTimeInMillis(),
                AttendanceObject.Choices.NEGATIVE);
        attendanceObject.id = dbHelper.addAttendance(attendanceObject);
        Log.i("New Attendance Record", "ID: "+attendanceObject.id);
        return attendanceObject;
    }

    private void sendNotification(AttendanceObject attendanceObject){
        Log.i("Notification", "Sent");
    }
}
