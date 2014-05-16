package com.montyskew.momentum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.content.DialogInterface;


public class Goal extends Activity
        implements OnClickListener, OnEditorActionListener {

    public static Integer goalPoints = 0;
    public static String goalPointsText;
    public static String goalText;
    private EditText goalEditText;
    private SharedPreferences saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        //reference widgets
        Button createGoal = (Button) findViewById(R.id.create_goal_button);
        Button deleteGoal = (Button) findViewById(R.id.delete_goal);
        goalEditText = (EditText) findViewById(R.id.create_goal_name);

        //set listeners
        createGoal.setOnClickListener(this);
        deleteGoal.setOnClickListener(this);
        goalEditText.setOnEditorActionListener(this);

        //Shared Preferences
        saved = getSharedPreferences("SavedValues", MODE_PRIVATE);

        //set visibility
        setGoalVisibility();
            }

    private void newGoal() {
        assert (goalEditText.getText()) != null;
        final String newGoal = (goalEditText.getText()).toString();
        if (newGoal.equals("")) {
            new AlertDialog.Builder(this)
                    .setMessage("Goal Name Is Empty")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        } else {
            final EditText points = new EditText(this);
            final AlertDialog.Builder check = new AlertDialog.Builder(this);
            points.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(this)
                    .setMessage("Enter Goal Points")
                    .setView(points)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            goalPointsText = (points.getText()).toString();
                            assert (points.getText()) != null;
                            if (points.equals("")) {
                                check.setMessage("Goal Points Empty");
                                check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        newGoal();
                                    }
                                });
                                check.show();
                            } else {
                                goalPoints = Integer.parseInt(goalPointsText);
                                goalText = newGoal;
                                Progress.startOver=false;
                                setGoalVisibility();
                            }
                        }
                    }).show();
        }
    }

    public void deleteGoal() {
        final EditText pwd = new EditText(this);
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder pass = new AlertDialog.Builder(this);
        final AlertDialog.Builder check = new AlertDialog.Builder(this);
        pass.setMessage("Enter Password");
        pass.setView(pwd);
        pass.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                assert (pwd.getText()) != null;
                String checkPwd = (pwd.getText()).toString();
                if (checkPwd.equals(Users.password)) {
                    goalText = null;
                    goalPoints = 0;
                    goalPointsText = null;
                    setGoalVisibility();
                } else {
                    check.setMessage("Password Incorrect");
                    check.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteGoal();
                        }
                    });
                    check.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    check.show();

                }
            }
        }).show();
    }

    public void setGoalVisibility() {
        if (Users.username == null) {
            View createGoal = findViewById(R.id.create_goal);
            View createTable = findViewById(R.id.create_goal_table);
            View exist = findViewById(R.id.goal_user_exist);
            View goal = findViewById(R.id.goal);
            View table = findViewById(R.id.goal_table);
            View button = findViewById(R.id.delete_goal);
            goal.setVisibility(View.INVISIBLE);
            exist.setVisibility(View.VISIBLE);
            createGoal.setVisibility(View.INVISIBLE);
            table.setVisibility(View.INVISIBLE);
            createTable.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
        } else if (goalText == null) {
            View createGoal = findViewById(R.id.create_goal);
            View createTable = findViewById(R.id.create_goal_table);
            View exist = findViewById(R.id.goal_user_exist);
            View goal = findViewById(R.id.goal);
            View table = findViewById(R.id.goal_table);
            View button = findViewById(R.id.delete_goal);
            EditText format = (EditText) findViewById(R.id.create_goal_name);
            format.setText("");
            exist.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.INVISIBLE);
            createGoal.setVisibility(View.VISIBLE);
            table.setVisibility(View.INVISIBLE);
            createTable.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
        } else {
            View createGoal = findViewById(R.id.create_goal);
            View createTable = findViewById(R.id.create_goal_table);
            View exist = findViewById(R.id.goal_user_exist);
            View goal = findViewById(R.id.goal);
            View table = findViewById(R.id.goal_table);
            View button = findViewById(R.id.delete_goal);
            TextView formatText = (TextView) findViewById(R.id.active_goal);
            TextView formatPoints = (TextView) findViewById(R.id.active_goal_points);
            formatText.setText(goalText);
            formatPoints.setText(goalPointsText);
            exist.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.VISIBLE);
            createGoal.setVisibility(View.INVISIBLE);
            table.setVisibility(View.VISIBLE);
            createTable.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onPause() {
        //save variables
        SharedPreferences.Editor editor = saved.edit();
        editor.putString("goalText", goalText);
        editor.putInt("goalPoints", goalPoints);
        editor.putString("goalPointsText", goalPointsText);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Progress.startOver) {
            setGoalVisibility();
        } else {
            goalText = saved.getString("goalText", goalText);
            goalPoints = saved.getInt("goalPoints", goalPoints);
            goalPointsText = saved.getString("goalPointsText", goalPointsText);
            setGoalVisibility();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_goal_button:
                newGoal();
                break;
            case R.id.delete_goal:
                deleteGoal();
                break;
        }
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
