package com.montyskew.momentum;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


public class Progress extends Activity {
    public int activityCount;
    public int goalProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Users user = new Users();
        //user.setUsersVisibility();
    }

    public void updateProgress(int goalProgress, int goal){
        ProgressBar mProgress=(ProgressBar) findViewById(R.id.progressBar);
        if(goalProgress<goal){
            mProgress.setProgress(goalProgress);
            mProgress.setMax(goal);
        }
        else if(goalProgress>=goal){
            mProgress.setProgress(goal);
            mProgress.setMax(goal);
        }
        else{
            mProgress.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.menu_progress:
                Intent progress = new Intent(this,Progress.class);
                startActivity(progress);
                return true;
            case R.id.menu_activities:
                Intent activities = new Intent(this,Activities.class);
                startActivity(activities);
                return true;
            case R.id.menu_goal:
                Intent goal = new Intent(this,Goal.class);
                startActivity(goal);
                return true;
            case R.id.menu_users:
                Intent users = new Intent(this,Users.class);
                startActivity(users);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}