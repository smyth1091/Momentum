package com.montyskew.momentum;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Progress extends Activity implements View.OnClickListener {
    private static ProgressBar mProgress;
    public static Boolean startOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        Button startOver = (Button) findViewById(R.id.start_over);
        startOver.setOnClickListener(this);
        setProgressVisibility();
    }

    public static void updateProgressBar() {
        if (Activities.activityPoints < Goal.goalPoints) {
            mProgress.setProgress(Activities.activityPoints);
            mProgress.setMax(Goal.goalPoints);
            mProgress.setVisibility(View.VISIBLE);
        } else if (Activities.activityPoints >= Goal.goalPoints) {
            mProgress.setProgress(Goal.goalPoints);
            mProgress.setMax(Goal.goalPoints);
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.INVISIBLE);
        }
    }

    private void startOver() {
        new AlertDialog.Builder(this)
                .setMessage("Are You Sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Activities.activityPoints = 0;
                        Goal.goalPoints = 0;
                        Goal.goalText = null;
                        Goal.goalPointsText = null;
                        startOver = true;
                        setProgressVisibility();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void setProgressVisibility() {
        if (Users.username == null) {
            View user = findViewById(R.id.progress_user_exist);
            View goal = findViewById(R.id.progress_goal_exist);
            View activity = findViewById(R.id.progress_activity_exist);
            View congrads = findViewById(R.id.progress_congrads);
            View progress = findViewById(R.id.progress_current);
            View progressValue = findViewById(R.id.progress_value);
            View progressBar = findViewById(R.id.progress_bar);
            View image = findViewById(R.id.progress_image);
            View button = findViewById(R.id.start_over);
            image.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            user.setVisibility(View.VISIBLE);
            goal.setVisibility(View.INVISIBLE);
            activity.setVisibility(View.INVISIBLE);
            congrads.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.INVISIBLE);
            progressValue.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else if (Goal.goalText == null) {
            View user = findViewById(R.id.progress_user_exist);
            View goal = findViewById(R.id.progress_goal_exist);
            View activity = findViewById(R.id.progress_activity_exist);
            View congrads = findViewById(R.id.progress_congrads);
            View progress = findViewById(R.id.progress_current);
            View progressValue = findViewById(R.id.progress_value);
            View progressBar = findViewById(R.id.progress_bar);
            View image = findViewById(R.id.progress_image);
            View button = findViewById(R.id.start_over);
            image.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            user.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.VISIBLE);
            activity.setVisibility(View.INVISIBLE);
            congrads.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.INVISIBLE);
            progressValue.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else if (Activities.activities.isEmpty() && Activities.activityPoints <= 0) {
            View user = findViewById(R.id.progress_user_exist);
            View goal = findViewById(R.id.progress_goal_exist);
            View activity = findViewById(R.id.progress_activity_exist);
            View congrads = findViewById(R.id.progress_congrads);
            View progress = findViewById(R.id.progress_current);
            View progressValue = findViewById(R.id.progress_value);
            View progressBar = findViewById(R.id.progress_bar);
            View image = findViewById(R.id.progress_image);
            View button = findViewById(R.id.start_over);
            image.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            user.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.INVISIBLE);
            activity.setVisibility(View.VISIBLE);
            congrads.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.INVISIBLE);
            progressValue.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else if (Activities.activityPoints >= Goal.goalPoints) {
            View user = findViewById(R.id.progress_user_exist);
            View goal = findViewById(R.id.progress_goal_exist);
            View activity = findViewById(R.id.progress_activity_exist);
            View congrads = findViewById(R.id.progress_congrads);
            View progress = findViewById(R.id.progress_current);
            View progressValue = findViewById(R.id.progress_value);
            View progressBar = findViewById(R.id.progress_bar);
            View image = findViewById(R.id.progress_image);
            View button = findViewById(R.id.start_over);
            image.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            user.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.INVISIBLE);
            activity.setVisibility(View.INVISIBLE);
            congrads.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
            progressValue.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            View user = findViewById(R.id.progress_user_exist);
            View goal = findViewById(R.id.progress_goal_exist);
            View activity = findViewById(R.id.progress_activity_exist);
            View congrads = findViewById(R.id.progress_congrads);
            View progress = findViewById(R.id.progress_current);
            View progressValue = findViewById(R.id.progress_value);
            TextView progressValueText = (TextView) findViewById(R.id.progress_value);
            View progressBar = findViewById(R.id.progress_bar);
            View image = findViewById(R.id.progress_image);
            View button = findViewById(R.id.start_over);
            image.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            user.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.INVISIBLE);
            activity.setVisibility(View.INVISIBLE);
            congrads.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            progressValue.setVisibility(View.VISIBLE);
            progressValueText.setText(Activities.activityPoints + " / " + Goal.goalPoints);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        //save variables
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProgressBar();
        setProgressVisibility();
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
        switch (item.getItemId()) {
            case R.id.menu_progress:
                Intent progress = new Intent(this, Progress.class);
                startActivity(progress);
                return true;
            case R.id.menu_activities:
                Intent activities = new Intent(this, Activities.class);
                startActivity(activities);
                return true;
            case R.id.menu_goal:
                Intent goal = new Intent(this, Goal.class);
                startActivity(goal);
                return true;
            case R.id.menu_users:
                Intent users = new Intent(this, Users.class);
                startActivity(users);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_over:
                startOver();
                break;
        }
    }
}
