package com.gvjay.classmanager;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.DBHelper;

public class UpdateAttendanceService extends IntentService {

    public static final String ACTION_POSITIVE = "Attended";
    public static final String ACTION_NEGATIVE = "Absent";
    public static final String ACTION_NEUTRAL = "Not Counted";
    public static final String ID_KEY = "Attendance ID";

    private long id;

    public UpdateAttendanceService(){
        super("UpdateAttendanceService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DBHelper dbHelper = new DBHelper(this);
        try {
            id = intent.getLongExtra(ID_KEY, -1);
            Log.i("UpdateAttendanceService", ""+id);
            switch (intent.getAction()){
                case ACTION_POSITIVE:
                    dbHelper.updateAttendanceByID(id, AttendanceObject.Choices.POSITIVE);
                    break;
                case ACTION_NEGATIVE:
                    dbHelper.updateAttendanceByID(id, AttendanceObject.Choices.NEGATIVE);
                    break;
                case ACTION_NEUTRAL:
                    dbHelper.updateAttendanceByID(id, AttendanceObject.Choices.NEUTRAL);
                    break;
            }
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.cancel(1729 + (int) id);
        }catch (java.lang.NullPointerException e){
            e.printStackTrace();
        }
    }
}
