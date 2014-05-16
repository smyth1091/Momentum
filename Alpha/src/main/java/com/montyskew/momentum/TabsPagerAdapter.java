package com.montyskew.momentum;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Set fragments for wipe
    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new Progress();
            case 1:
                return new Activities();
            case 2:
                return new Goal();
            case 3:
                return new Users();
        }

        return null;
    }

    //Return fragment
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    //Fragment count
    @Override
    public int getCount() {
        return 4;
    }

}
