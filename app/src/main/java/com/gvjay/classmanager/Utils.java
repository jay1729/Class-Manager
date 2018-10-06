package com.gvjay.classmanager;

import android.text.format.DateUtils;

import com.gvjay.classmanager.Database.AttendanceObject;

import java.util.ArrayList;

public class Utils {
    public static int getDayNumber(String day){
        String[] days = Consts.daysOfTheWeek;
        for(int i=0;i<7;i++){
            if(day.equals(days[i])) return i;
        }
        return -1;
    }

    public static String getDayFromNumber(int day){
        return Consts.daysOfTheWeek[day];
    }

    public static double calculateAttendance(ArrayList<AttendanceObject> attendanceObjects){
        int s = attendanceObjects.size();
        double n = 0;
        double d = 0;
        for(int i=0;i<s;i++){
            d++;
            if(attendanceObjects.get(i).status.equals(AttendanceObject.Choices.POSITIVE)) n++;
            if(attendanceObjects.get(i).status.equals(AttendanceObject.Choices.NEUTRAL)) d--;
        }
        if(d == 0) return -1;
        return (100*n)/d;
    }

    public static String getTiming(long fromTime, long toTime){
        String from = getTime(fromTime);
        String to = getTime(toTime);
        if(from.substring(from.length()-2, from.length()).equals(to.substring(to.length()-2, to.length()))){
            from = from.substring(0, from.length()-3);
        }
        return from + "-" + to;
    }

    private static String getTime(long timeInMillis){
        boolean isAM = true;

        long hour = timeInMillis / DateUtils.HOUR_IN_MILLIS;
        long minute = (timeInMillis % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
        if(hour >= 12) isAM = false;
        if(hour >= 13) hour -= 12;

        String output = "";
        output += String.valueOf(hour);

        if(minute != 0){
            output += ":";
            if(minute < 10) output += "0";
            output += String.valueOf(minute);
        }
        output += " ";
        if(isAM) output += "AM";
        else output += "PM";
        return output;
    }
}
