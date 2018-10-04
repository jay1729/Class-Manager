package com.gvjay.classmanager;

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
}
