package com.montyskew.momentum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class Activities extends Activity implements View.OnClickListener, TextView.OnEditorActionListener, Serializable {
    public static TreeMap<String, Integer> activities = new TreeMap<String, Integer>();
    public ArrayList<String> activityList = new ArrayList<String>();
    public EditText newActivity;
    public ArrayAdapter adapter;
    public ListView listView;
    public static Integer activityPoints = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            activities = (TreeMap) bundle.getSerializable("activities");
            activityList = bundle.getStringArrayList("activitiesList");
            activityPoints = bundle.getInt("activityCumulativePoints");
        }
        updateArrayList();
        setContentView(R.layout.activity_activities);
        listView = (ListView) findViewById(R.id.activities_table);

        newActivity = (EditText) findViewById(R.id.create_activity_name);
        Button addActivity = (Button) findViewById(R.id.create_activity_button);
        Button verifyActivity = (Button) findViewById(R.id.verify_activity_button);

        addActivity.setOnClickListener(this);
        verifyActivity.setOnClickListener(this);
        newActivity.setOnEditorActionListener(this);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, android.R.id.text1, activityList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        updateActivityTable();
        setActivitiesVisibility();

    }

    public void createActivity() {
        assert (newActivity.getText()) != null;
        final String activityName = (newActivity.getText()).toString();
        if (activityName.equals("")) {
            new AlertDialog.Builder(this)
                    .setMessage("Activity Name Is Empty")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            final EditText points = new EditText(this);
            final AlertDialog.Builder check = new AlertDialog.Builder(this);
            points.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(this)
                    .setMessage("Enter Activity Points")
                    .setView(points)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String activityPointsText = (points.getText()).toString();
                            assert (points.getText()) != null;
                            if (activityPointsText.equals("")) {
                                check.setMessage("Activity Points Empty");
                                check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        createActivity();
                                    }
                                });
                                check.show();
                            } else {
                                Integer activityPoints = Integer.parseInt(activityPointsText);
                                activities.put(activityName, activityPoints);
                                setActivitiesVisibility();
                                activityList.add(activityName);
                                updateActivityTable();
                                newActivity.setText("");
                            }
                        }
                    }).show();
        }
    }

    private void updateArrayList() {
        if (!activities.isEmpty()) {
            Set keys = activities.keySet();
            Iterator i = keys.iterator();
            while (i.hasNext()) {
                String s = (String) i.next();
                activityList.add(s);
            }
        }
    }

    public void verifyActivity() {
        final EditText pwd = new EditText(this);
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final SparseBooleanArray checked = listView.getCheckedItemPositions();
        final AlertDialog.Builder pass = new AlertDialog.Builder(this);
        final AlertDialog.Builder check = new AlertDialog.Builder(this);
        final AlertDialog.Builder size = new AlertDialog.Builder(this);
        if (checked.size() == 0) {
            size.setMessage("No Items Checked");
            size.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            size.show();
        } else {
            pass.setMessage("Enter Password");
            pass.setView(pwd);
            pass.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    assert (pwd.getText()) != null;
                    String checkPwd = (pwd.getText()).toString();
                    if (checkPwd.equals(Users.password)) {
                        for (int i = 0; i < checked.size(); i++) {
                            int position = checked.keyAt(i);
                            if (checked.valueAt(i)) {
                                String name = (String.valueOf(adapter.getItem(position)));
                                updateProgress(name);
                            }
                        }
                    } else {
                        check.setMessage("Password Incorrect");
                        check.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                verifyActivity();
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
    }

    public void updateActivityTable() {
        adapter.notifyDataSetChanged();
    }

    public void updateProgress(String name) {
        Integer points = activities.get(name);
        activityPoints += points;
        activities.remove(name);
        activityList.remove(name);
        Progress.updateProgressBar();
        updateActivityTable();
    }

    public void setActivitiesVisibility() {
        if (Users.username == null) {
            View createUser = findViewById(R.id.activities_user_exist);
            View createGoal = findViewById(R.id.activities_goal_exist);
            View createActivity = findViewById(R.id.create_activity_table);
            View addActivity = findViewById(R.id.add_activity);
            View activitiesText = findViewById(R.id.activities);
            View activitiesTable = findViewById(R.id.activities_table);
            View verifyButton = findViewById(R.id.verify_activity_button);
            createUser.setVisibility(View.VISIBLE);
            createGoal.setVisibility(View.INVISIBLE);
            createActivity.setVisibility(View.INVISIBLE);
            addActivity.setVisibility(View.INVISIBLE);
            activitiesText.setVisibility(View.INVISIBLE);
            activitiesTable.setVisibility(View.INVISIBLE);
            verifyButton.setVisibility(View.INVISIBLE);
        } else if (Goal.goalText == null) {
            View createUser = findViewById(R.id.activities_user_exist);
            View createGoal = findViewById(R.id.activities_goal_exist);
            View createActivity = findViewById(R.id.create_activity_table);
            View addActivity = findViewById(R.id.add_activity);
            View activitiesText = findViewById(R.id.activities);
            View activitiesTable = findViewById(R.id.activities_table);
            View verifyButton = findViewById(R.id.verify_activity_button);
            createUser.setVisibility(View.INVISIBLE);
            createGoal.setVisibility(View.VISIBLE);
            createActivity.setVisibility(View.INVISIBLE);
            addActivity.setVisibility(View.INVISIBLE);
            activitiesText.setVisibility(View.INVISIBLE);
            activitiesTable.setVisibility(View.INVISIBLE);
            verifyButton.setVisibility(View.INVISIBLE);
        } else if (checkEmpty(activities)) {
            View createUser = findViewById(R.id.activities_user_exist);
            View createGoal = findViewById(R.id.activities_goal_exist);
            View createActivity = findViewById(R.id.create_activity_table);
            View addActivity = findViewById(R.id.add_activity);
            View activitiesText = findViewById(R.id.activities);
            View activitiesTable = findViewById(R.id.activities_table);
            View verifyButton = findViewById(R.id.verify_activity_button);
            createUser.setVisibility(View.INVISIBLE);
            createGoal.setVisibility(View.INVISIBLE);
            createActivity.setVisibility(View.VISIBLE);
            addActivity.setVisibility(View.VISIBLE);
            activitiesText.setVisibility(View.INVISIBLE);
            activitiesTable.setVisibility(View.INVISIBLE);
            verifyButton.setVisibility(View.INVISIBLE);
        } else {
            View createUser = findViewById(R.id.activities_user_exist);
            View createGoal = findViewById(R.id.activities_goal_exist);
            View createActivity = findViewById(R.id.create_activity_table);
            View addActivity = findViewById(R.id.add_activity);
            View activitiesText = findViewById(R.id.activities);
            View activitiesTable = findViewById(R.id.activities_table);
            View verifyButton = findViewById(R.id.verify_activity_button);
            createUser.setVisibility(View.INVISIBLE);
            createGoal.setVisibility(View.INVISIBLE);
            createActivity.setVisibility(View.VISIBLE);
            addActivity.setVisibility(View.VISIBLE);
            activitiesText.setVisibility(View.VISIBLE);
            activitiesTable.setVisibility(View.VISIBLE);
            verifyButton.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkEmpty(TreeMap map) {
        if (map != null) {
            if (map.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putSerializable("activities", activities);
        state.putInt("activityCumulativePoints", activityPoints);
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        activities = (TreeMap) state.getSerializable("activities");
        updateArrayList();
        activityPoints = state.getInt("activityCumulativePoints");
        updateActivityTable();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_activity_button:
                createActivity();
                break;
            case R.id.verify_activity_button:
                verifyActivity();
                break;
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
