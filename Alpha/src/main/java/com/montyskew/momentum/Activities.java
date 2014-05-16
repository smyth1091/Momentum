package com.montyskew.momentum;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class Activities extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener, Serializable {
    //Map to hold activity name and point value
    public static TreeMap<String, Integer> activities = new TreeMap<String, Integer>();
    //Array to hold activity name for list
    public ArrayList<String> activityList = new ArrayList<String>();
    public ArrayAdapter adapter;
    public static Integer activityPoints = 0;
    private View view;
    private static EditText newActivity;
    private static TextView userExist;
    private static TextView exist;
    private static TableLayout activity;
    private static TextView addActivityText;
    private static TextView activitiesText;
    private static ListView listView;
    private static Button verifyActivity;
    private static Button addActivity;

    //Create page
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout
        view = inflater.inflate(R.layout.activity_activities, container, false);
        //Assign widgets
        listView = (ListView) view.findViewById(R.id.activities_table);
        userExist = (TextView) view.findViewById(R.id.activity_user_exist);
        exist = (TextView) view.findViewById(R.id.activity_goal_exist);
        addActivityText = (TextView) view.findViewById(R.id.add_activity);
        activitiesText = (TextView) view.findViewById(R.id.activities);
        activity = (TableLayout) view.findViewById(R.id.create_activity_table);
        newActivity = (EditText) view.findViewById(R.id.create_activity_name);
        addActivity = (Button) view.findViewById(R.id.create_activity_button);
        verifyActivity = (Button) view.findViewById(R.id.verify_activity_button);
        //Set listeners
        addActivity.setOnClickListener(this);
        verifyActivity.setOnClickListener(this);
        newActivity.setOnEditorActionListener(this);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_checked, android.R.id.text1, activityList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        //Fill activity list table
        updateActivityTable();
        //Set widget visibility
        setActivitiesVisibility();
        return view;

    }

    //Prompt to create activity
    public void createActivity() {
        assert (newActivity.getText()) != null;
        final String activityName = (newActivity.getText()).toString();
        //Check empty field
        if (activityName.equals("")) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Activity Name Is Empty")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            final EditText points = new EditText(getActivity());
            final AlertDialog.Builder check = new AlertDialog.Builder(getActivity());
            points.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(getActivity())
                    .setMessage("Enter Activity Points")
                    .setView(points)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String activityPointsText = (points.getText()).toString();
                            assert (points.getText()) != null;
                            //Check for bad data
                            if (activityPointsText.equals("") || !isInt(activityPointsText)) {
                                check.setMessage("Activity Points Empty/Not A Number");
                                check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        createActivity();
                                    }
                                });
                                check.show();
                            } else {
                                //Add activity
                                Integer activityPoints = Integer.parseInt(activityPointsText);
                                //Add name and points to map
                                activities.put(activityName, activityPoints);
                                setActivitiesVisibility();
                                //Add name to array
                                activityList.add(activityName);
                                updateActivityTable();
                                newActivity.setText("");
                            }
                        }
                    }).show();
        }
    }

    //Verify integer input
    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    //Rebuilds list if arrayList is null(currently not in use)
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

    //Prompt for activity verification
    public void verifyActivity() {
        final EditText pwd = new EditText(getActivity());
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final SparseBooleanArray checked = listView.getCheckedItemPositions();
        final AlertDialog.Builder pass = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder check = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder size = new AlertDialog.Builder(getActivity());
        //Ensure item is checked
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
                    //Check password
                    if (checkPwd.equals(Users.password)) {
                        //Determine which activity name is checked
                        for (int i = 0; i < checked.size(); i++) {
                            int position = checked.keyAt(i);
                            if (checked.valueAt(i)) {
                                String name = (String.valueOf(adapter.getItem(position)));
                                //Add points to progress
                                updateProgress(name);
                                setActivitiesVisibility();
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

    //Notify list of data change
    public void updateActivityTable() {
        adapter.notifyDataSetChanged();
    }

    //Update progress
    public void updateProgress(String name) {
        Integer points = activities.get(name);
        activityPoints += points;
        activities.remove(name);
        activityList.remove(name);
        Progress.updateProgressBar();
        Progress.setProgressVisibility();
        updateActivityTable();
    }

    //Set widget visibility
    public static void setActivitiesVisibility() {
        if (Users.username == null) {
            userExist.setVisibility(View.VISIBLE);
            exist.setVisibility(View.INVISIBLE);
            addActivityText.setVisibility(View.INVISIBLE);
            activity.setVisibility(View.INVISIBLE);
            addActivity.setVisibility(View.INVISIBLE);
            activitiesText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            verifyActivity.setVisibility(View.INVISIBLE);
        } else if (Goal.goalPointsText == null) {
            userExist.setVisibility(View.INVISIBLE);
            exist.setVisibility(View.VISIBLE);
            addActivityText.setVisibility(View.INVISIBLE);
            activity.setVisibility(View.INVISIBLE);
            addActivity.setVisibility(View.INVISIBLE);
            activitiesText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            verifyActivity.setVisibility(View.INVISIBLE);
        } else if (checkEmpty(activities)) {
            userExist.setVisibility(View.INVISIBLE);
            exist.setVisibility(View.INVISIBLE);
            addActivityText.setVisibility(View.VISIBLE);
            activity.setVisibility(View.VISIBLE);
            addActivity.setVisibility(View.VISIBLE);
            activitiesText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            verifyActivity.setVisibility(View.INVISIBLE);
        } else {
            userExist.setVisibility(View.INVISIBLE);
            exist.setVisibility(View.INVISIBLE);
            addActivityText.setVisibility(View.VISIBLE);
            activity.setVisibility(View.VISIBLE);
            addActivity.setVisibility(View.VISIBLE);
            activitiesText.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            verifyActivity.setVisibility(View.VISIBLE);
        }
    }

    //Check if map is empty
    public static boolean checkEmpty(TreeMap map) {
        if (map != null) {
            if (map.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    //Save data in bundle
    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putSerializable("activities", activities);
        state.putInt("activityCumulativePoints", activityPoints);
        super.onSaveInstanceState(state);
    }

    //Button actions
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
