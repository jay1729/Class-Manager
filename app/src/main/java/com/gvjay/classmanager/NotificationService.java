package com.gvjay.classmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.gvjay.classmanager.Database.AttendanceObject;

public class NotificationService extends Service {

    public static String CHANNEL_NAME = "gumballi";
    public static String CHANNEL_ID = "1729";
    public static String CHANNEL_DESC = "Channel for Class Manager";
    public static String NOTIFICATION_TITLE = "Class Manager";
    public static String NOTIFICATION_TEXT = "Did You Attend ";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BackTask.executeTask(this);
        BackTask.setNotificationService(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    public void sendNotification(AttendanceObject attendanceObject){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle(NOTIFICATION_TITLE);
        builder.setContentText(getContentText(attendanceObject));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        setNotificationActions(builder, attendanceObject);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify((1729 + (int) attendanceObject.id), builder.build());
    }

    public static void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private String getContentText(AttendanceObject attendanceObject){
        return NOTIFICATION_TEXT + attendanceObject.classTitle + " ?";
    }

    private void setNotificationActions(NotificationCompat.Builder builder, AttendanceObject attendanceObject){
        String[] actions = { UpdateAttendanceService.ACTION_POSITIVE, UpdateAttendanceService.ACTION_NEGATIVE,
                UpdateAttendanceService.ACTION_NEUTRAL };
        String[] btnTexts = { AttendanceObject.Choices.POSITIVE, AttendanceObject.Choices.NEGATIVE, AttendanceObject.Choices.NEUTRAL };
        for(int i=0;i<3;i++){
            Intent intent = new Intent(this, UpdateAttendanceService.class);
            intent.setAction(actions[i]);
            intent.putExtra(UpdateAttendanceService.ID_KEY, attendanceObject.id);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
