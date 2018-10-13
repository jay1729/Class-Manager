package com.gvjay.classmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AttendanceEntryWorker extends Worker {

    public static String CLASS_ID_KEY = "Class_ID";
    public static String WORK_TAG = "Attendance Entry Worker";

    private UUID notificationWRID;
    private DBHelper dbHelper;

    public AttendanceEntryWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        long id = data.getLong(CLASS_ID_KEY, -1);
        ClassObject classObject = dbHelper.getClassByID(id);
        AttendanceObject attendanceObject = new AttendanceObject(classObject.title, Calendar.getInstance().getTimeInMillis(),
                Utils.getDefaultAttendanceStatus());
        attendanceObject.id = dbHelper.addAttendance(attendanceObject);
        enqueueNotification(attendanceObject.id, 0);
        return Result.SUCCESS;
    }

    private void enqueueNotification(long id, long delayInMillis){

        Data inputData = new Data.Builder().putLong(AttendanceNotificationWorker.ATTENDANCE_ID_KEY, id).build();
        OneTimeWorkRequest notificationWorkRequest = new OneTimeWorkRequest.Builder(AttendanceNotificationWorker.class)
                .setInputData(inputData)
                .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                .addTag(AttendanceNotificationWorker.WORK_TAG)
                .build();
        WorkManager.getInstance().enqueue(notificationWorkRequest);
        notificationWRID = notificationWorkRequest.getId();
    }
}
