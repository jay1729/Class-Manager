package com.gvjay.classmanager.DebugUtils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.gvjay.classmanager.R;

public class DebugNotifier {
    public static String CHANNEL_NAME = "gumballi debugger";
    public static String CHANNEL_ID = "1730";
    public static String CHANNEL_DESC = "Channel for Class Manager debugging";

    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_TEXT;
    private Context context;

    public DebugNotifier(Context context){
        this.context = context;
        NOTIFICATION_TITLE = "Default";
        NOTIFICATION_TEXT = "Default";
    }

    public void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle(NOTIFICATION_TITLE);
        builder.setContentText(NOTIFICATION_TEXT);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1000, builder.build());
    }

    public void setTitle(String title){
        NOTIFICATION_TITLE = title;
    }

    public void setText(String text){
        NOTIFICATION_TEXT = text;
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
}
