package com.montyskew.momentum;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

import android.app.ActionBar.Tab;

public class MomentumTabsAdapter extends FragmentPagerAdapter implements
        ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private final Context myContext;
    private final ActionBar myActionBar;
    private final ViewPager myViewPager;
    private final ArrayList<TabInfo> myTabs = new ArrayList<TabInfo>();
    private final String TAG = "21st Polling:";

    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

    public MomentumTabsAdapter(FragmentActivity fa, ViewPager pager) {
        super(fa.getSupportFragmentManager());
        myContext = fa;
        myActionBar = fa.getActionBar();
        myViewPager = pager;
        myViewPager.setAdapter(this);
        myViewPager.setOnPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        myTabs.add(info);
        myActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        myActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
        myViewPager.setCurrentItem(tab.getPosition());
        Log.v(TAG, "clicked");
        Object tag = tab.getTag();
        for (int i = 0; i < myTabs.size(); i++) {
            if (myTabs.get(i) == tag) {
                myViewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = myTabs.get(position);
        return Fragment.instantiate(myContext, info.clss.getName(), info.args);
    }

    @Override
    public int getCount() {
        return myTabs.size();
    }
}
