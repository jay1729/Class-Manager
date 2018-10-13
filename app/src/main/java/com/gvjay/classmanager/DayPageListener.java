package com.gvjay.classmanager;

import android.support.v4.view.ViewPager;

public class DayPageListener implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private PageChangeNotify pageChangeNotify;

    public DayPageListener(ViewPager viewPager, PageChangeNotify pageChangeNotify){
        this.viewPager = viewPager;
        this.pageChangeNotify = pageChangeNotify;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if(i == 0){
            viewPager.setCurrentItem(7, false);
            pageChangeNotify.notifyPageChanged(7);
        }else if(i == 8){
            viewPager.setCurrentItem(1, false);
            pageChangeNotify.notifyPageChanged(1);
        }else{
            pageChangeNotify.notifyPageChanged(i);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
