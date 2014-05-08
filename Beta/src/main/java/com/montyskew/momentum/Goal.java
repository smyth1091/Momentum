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

    private int goalPoints;
    private String goalText;
    private EditText goalEditText;
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        //set users visibility
        //setVisibility();

        //reference widgets
        Button createGoal = (Button) findViewById(R.id.new_goal);
        goalEditText = (EditText) findViewById(R.id.goal_name);

        //set listeners
        createGoal.setOnClickListener(this);
        goalEditText.setOnEditorActionListener(this);

        //Shared Preferences
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    private void newGoal() {
        assert (goalEditText.getText()) != null;
        goalText = (goalEditText.getText()).toString();
        if (goalText.equals("")) {
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
                            assert (points.getText()) != null;
                            goalPoints = Integer.parseInt(points.getText().toString());
                            if (points.equals("")) {
                                check.setMessage("Goal Points Empty");
                                check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        newGoal();
                                    }
                                });
                                check.show();
                            }
                        }
                    }).show();
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
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("goalText", goalText);
        editor.putInt("goalPoints", goalPoints);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Resume Variables
        goalText = savedValues.getString("goalText", goalText);
        goalPoints = savedValues.getInt("goalPoints", goalPoints);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_goal:
                newGoal();
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
