package com.montyskew.momentum;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

    private ViewPager myViewPager;
    private MomentumTabsAdapter myTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        myViewPager = new ViewPager(this);
        myViewPager.setId(R.id.pager);
        setContentView(myViewPager);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0,ActionBar.DISPLAY_SHOW_TITLE);

        myTabsAdapter = new MomentumTabsAdapter(this,myViewPager);
        myTabsAdapter.addTab(bar.newTab().setText("Progress"),Progress.class,null);
        myTabsAdapter.addTab(bar.newTab().setText("Activities"),Activities.class,null);
        myTabsAdapter.addTab(bar.newTab().setText("Goal"),Goal.class,null);
        myTabsAdapter.addTab(bar.newTab().setText("Users"),Users.class,null);

        if(savedInstanceState != null){
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab",0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("tab",getActionBar().getSelectedNavigationIndex());
    }
}
