package com.gvjay.classmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;

import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;
import com.gvjay.classmanager.DebugUtils.DebugNotifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkerDispatcher extends Worker {

    public static String WORK_TAG = "Work Dispatcher";
    private Context context;

    public WorkerDispatcher(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Calendar calendar = Calendar.getInstance();
        int day;
        day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.i("WorkerDispatcher", calendar.get(Calendar.HOUR_OF_DAY) + "");
        long now = (calendar.get(Calendar.HOUR_OF_DAY)*DateUtils.HOUR_IN_MILLIS) + (calendar.get(Calendar.MINUTE)*DateUtils.MINUTE_IN_MILLIS);
        Log.i("Next WorkerDispatcher","Will execute in " + ((DateUtils.DAY_IN_MILLIS - now) / DateUtils.MINUTE_IN_MILLIS) + " Minutes");
        DBHelper dbHelper = new DBHelper(context);
        ArrayList<ClassObject> classObjects = dbHelper.getClassesOnDay(day);
        int size = classObjects.size();
        for(int i=0;i<size;i++){
            if(classObjects.get(i).toTime - now <= 0) continue;
            Log.i("ClassObject", classObjects.get(i).id + " " + classObjects.get(i).toTime + " " + now);
            enqueueAttendanceEntryWorker(classObjects.get(i).id, classObjects.get(i).toTime - now);
        }
        enqueueNextWorkerDispatcher(DateUtils.DAY_IN_MILLIS - now);
        return Result.SUCCESS;
    }

    private void enqueueAttendanceEntryWorker(long id, long delayInMillis){
        Data inputData = new Data.Builder().putLong(AttendanceEntryWorker.CLASS_ID_KEY, id).build();
        OneTimeWorkRequest attendanceEntryWorkRequest = new OneTimeWorkRequest.Builder(AttendanceEntryWorker.class)
                .setInputData(inputData)
                .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                .addTag(AttendanceEntryWorker.WORK_TAG)
                .build();
        WorkManager.getInstance().enqueue(attendanceEntryWorkRequest);
    }

    public static void enqueueNextWorkerDispatcher(long delayInMillis){
        OneTimeWorkRequest nextWorkerDispatcherRequest = new OneTimeWorkRequest.Builder(WorkerDispatcher.class)
                .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG)
                .build();
        WorkManager.getInstance().enqueue(nextWorkerDispatcherRequest);
    }
}
