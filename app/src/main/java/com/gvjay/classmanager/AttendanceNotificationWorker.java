package com.gvjay.classmanager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.gvjay.classmanager.Database.AttendanceObject;
import com.gvjay.classmanager.Database.DBHelper;

import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AttendanceNotificationWorker extends Worker {

    public static String ATTENDANCE_ID_KEY = "Attendance_ID";
    public static String WORK_TAG = "Attendance Notification";
    public static String CHANNEL_NAME = "gumballi";
    public static String CHANNEL_ID = "1729";
    public static String CHANNEL_DESC = "Channel for Class Manager";
    public static String NOTIFICATION_TITLE = "Class Manager";
    public static String NOTIFICATION_TEXT = "Did You Attend ";

    private Context context;

    public AttendanceNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        DBHelper dbHelper = new DBHelper(context);
        AttendanceObject attendanceObject = dbHelper.getAttendanceByID(getInputData().getLong(ATTENDANCE_ID_KEY, -1));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_stat_cm);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(NOTIFICATION_TITLE);
        builder.setContentText(getContentText(attendanceObject));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        setNotificationActions(builder, attendanceObject);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify((1729 + (int) attendanceObject.id), builder.build());

        return Result.SUCCESS;
    }

    private String getContentText(AttendanceObject attendanceObject){
        return NOTIFICATION_TEXT + attendanceObject.classTitle + " ?";
    }

    private void setNotificationActions(NotificationCompat.Builder builder, AttendanceObject attendanceObject){
        String[] actions = { UpdateAttendanceService.ACTION_POSITIVE, UpdateAttendanceService.ACTION_NEGATIVE,
                UpdateAttendanceService.ACTION_NEUTRAL };
        String[] btnTexts = { AttendanceObject.Choices.POSITIVE, AttendanceObject.Choices.NEGATIVE, AttendanceObject.Choices.NEUTRAL };
        for(int i=0;i<3;i++){
            Intent intent = new Intent(context, UpdateAttendanceService.class);
            intent.setAction(actions[i]);
            intent.putExtra(UpdateAttendanceService.ID_KEY, attendanceObject.id);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            addActionToBuilder(builder, R.drawable.ic_launcher_background, btnTexts[i],
                    pendingIntent);
        }
    }

    private static void addActionToBuilder(NotificationCompat.Builder builder, int icon, String btnText, PendingIntent pendingIntent){
        if(Build.VERSION.SDK_INT < 20){
            builder.addAction(icon, btnText, pendingIntent);
        }else{
            NotificationCompat.Action action = new NotificationCompat.Action(icon, btnText, pendingIntent);
            builder.addAction(action);
        }
    }

}
