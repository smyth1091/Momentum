package com.montyskew.momentum;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.content.DialogInterface;


public class Goal extends Fragment
        implements OnClickListener, OnEditorActionListener {

    public static Integer goalPoints = 0;
    public static String goalPointsText;
    public static String goalText;
    private EditText goalEditText;
    private View view;
    private static TextView createGoalText;
    private static TableLayout createTable;
    private static TextView exist;
    private static TextView goal;
    private static TableLayout table;
    private static Button deleteGoal;
    private static Button createGoal;
    private static TextView formatText;
    private static TextView formatPoints;


    //Create view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout
        view = inflater.inflate(R.layout.activity_goal, container, false);
        //Assign widgets
        createGoalText = (TextView) view.findViewById(R.id.create_goal);
        createTable = (TableLayout) view.findViewById(R.id.create_goal_table);
        exist = (TextView) view.findViewById(R.id.goal_user_exist);
        goal = (TextView) view.findViewById(R.id.goal);
        table = (TableLayout) view.findViewById(R.id.goal_table);
        createGoal = (Button) view.findViewById(R.id.create_goal_button);
        deleteGoal = (Button) view.findViewById(R.id.delete_goal);
        goalEditText = (EditText) view.findViewById(R.id.create_goal_name);
        formatText = (TextView) view.findViewById(R.id.active_goal);
        formatPoints = (TextView) view.findViewById(R.id.active_goal_points);
        //Set listeners
        createGoal.setOnClickListener(this);
        deleteGoal.setOnClickListener(this);
        goalEditText.setOnEditorActionListener(this);
        //Set visibility
        setGoalVisibility();
        return view;
    }

    //New goal dialog
    private void newGoal() {
        assert (goalEditText.getText()) != null;
        final String newGoal = (goalEditText.getText()).toString();
        //Check for empty string
        if (newGoal.equals("")) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Goal Name Is Empty")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        } else {
            final EditText points = new EditText(getActivity());
            final AlertDialog.Builder check = new AlertDialog.Builder(getActivity());
            points.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(getActivity())
                    .setMessage("Enter Goal Points")
                    .setView(points)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            goalPointsText = (points.getText()).toString();
                            assert (points.getText()) != null;
                            //Check for bad input
                            if (points.equals("") || !isInt(goalPointsText)) {
                                check.setMessage("Goal Points Empty/Not A Number");
                                check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        newGoal();
                                    }
                                });
                                check.show();
                            } else {
                                //Set goal points and name
                                goalPoints = Integer.parseInt(goalPointsText);
                                goalText = newGoal;
                                //Reset start over state
                                Progress.startOver = false;
                                //Update visibility
                                setGoalVisibility();
                                Activities.setActivitiesVisibility();
                            }
                        }
                    }).show();
        }
    }

    //Check if input is int
    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    //Delete goal dialog
    public void deleteGoal() {
        final EditText pwd = new EditText(getActivity());
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder pass = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder check = new AlertDialog.Builder(getActivity());
        pass.setMessage("Enter Password");
        pass.setView(pwd);
        pass.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                assert (pwd.getText()) != null;
                String checkPwd = (pwd.getText()).toString();
                //Check password
                if (checkPwd.equals(Users.password)) {
                    goalText = null;
                    goalPoints = 0;
                    goalPointsText = null;
                    goalEditText.setText("");
                    setGoalVisibility();
                    Activities.setActivitiesVisibility();
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

    //Set goal visibility
    public static void setGoalVisibility() {
        if (Users.username == null) {
            createGoalText.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.INVISIBLE);
            exist.setVisibility(View.VISIBLE);
            createGoal.setVisibility(View.INVISIBLE);
            table.setVisibility(View.INVISIBLE);
            createTable.setVisibility(View.INVISIBLE);
        } else if (goalText == null) {
            createGoalText.setVisibility(View.VISIBLE);
            goal.setVisibility(View.INVISIBLE);
            exist.setVisibility(View.INVISIBLE);
            createGoal.setVisibility(View.VISIBLE);
            table.setVisibility(View.INVISIBLE);
            createTable.setVisibility(View.VISIBLE);
        } else {
            createGoalText.setVisibility(View.INVISIBLE);
            goal.setVisibility(View.VISIBLE);
            exist.setVisibility(View.INVISIBLE);
            createGoal.setVisibility(View.INVISIBLE);
            table.setVisibility(View.VISIBLE);
            createTable.setVisibility(View.VISIBLE);
            formatText.setText(goalText);
            formatPoints.setText(goalPointsText);

        }
    }

    //Bundle goal data on exit
    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putString("goalText", goalText);
        state.putInt("goalPoints", goalPoints);
        state.putString("goalPointsText", goalPointsText);
        super.onSaveInstanceState(state);
    }

    @Override
    public void onPause() {
        super.onPause();
        setGoalVisibility();
    }

    @Override
    public void onResume() {
        setGoalVisibility();
        super.onResume();

    }

    //Button actions
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
