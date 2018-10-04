package com.gvjay.classmanager;

import android.support.v4.view.ViewPager;

public class DayPageListener implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    public DayPageListener(ViewPager viewPager){
        this.viewPager = viewPager;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if(i == 0){
            viewPager.setCurrentItem(7, false);
        }
        if(i == 8){
            viewPager.setCurrentItem(1, false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
