package com.gvjay.classmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class DayAdapter extends FragmentPagerAdapter {

    private ArrayList<DayFragment> dayFragments = new ArrayList<>();

    public DayAdapter(FragmentManager fm) {
        super(fm);
        for(int i=0;i<9;i++){
            int realIndex = i;
            if(realIndex == 0){
                realIndex = 6;
            }else if(realIndex == 8){
                realIndex = 0;
            }else realIndex--;

            Bundle bundle = new Bundle();
            bundle.putInt(DayFragment.DAY_KEY, realIndex);

            DayFragment dayFragment = new DayFragment();
            dayFragment.setArguments(bundle);

            dayFragments.add(dayFragment);
        }
    }

    @Override
    public Fragment getItem(int i) {
        return dayFragments.get(i);
    }

    @Override
    public int getCount() {
        return 9;
    }

    public static int getRealDayNumber(int i){
        int realIndex = i;
        if(realIndex == 0){
            realIndex = 6;
        }else if(realIndex == 8){
            realIndex = 0;
        }else realIndex--;
        return realIndex;
    }
}
