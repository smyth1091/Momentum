package com.montyskew.momentum;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Progress extends Fragment implements View.OnClickListener {
    private static ProgressBar mProgress;
    public static Boolean startOver = false;
    private View view;
    private static TextView instructions;
    private static TextView congrads;
    private static TextView progress;
    private static TextView progressValue;
    private static ImageView image;
    private static Button startOverButton;

    //Create page
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout
        view = inflater.inflate(R.layout.activity_progress, container, false);
        //Assign widgets
        instructions = (TextView) view.findViewById(R.id.instructions);
        mProgress = (ProgressBar) view.findViewById(R.id.progress_bar);
        congrads = (TextView) view.findViewById(R.id.progress_congrads);
        progress = (TextView) view.findViewById(R.id.progress_current);
        progressValue = (TextView) view.findViewById(R.id.progress_value);
        image = (ImageView) view.findViewById(R.id.progress_image);
        startOverButton = (Button) view.findViewById(R.id.start_over);
        startOverButton.setOnClickListener(this);
        //Set visibility
        updateProgressBar();
        setProgressVisibility();
        return view;
    }

    //Set progress bar visibility
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

    //Start over dialog
    private void startOver() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are You Sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Activities.activityPoints = 0;
                        Goal.goalPoints = 0;
                        Goal.goalText = null;
                        Goal.goalPointsText = null;
                        startOver = true;
                        setProgressVisibility();
                        Activities.setActivitiesVisibility();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //Set widget visibility
    public static void setProgressVisibility() {
        if (Activities.activities.isEmpty() && Activities.activityPoints <= 0) {
            instructions.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);
            startOverButton.setVisibility(View.INVISIBLE);
            congrads.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.INVISIBLE);
            progressValue.setVisibility(View.INVISIBLE);
            mProgress.setVisibility(View.INVISIBLE);
        } else if (Activities.activityPoints >= Goal.goalPoints) {
            instructions.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
            startOverButton.setVisibility(View.VISIBLE);
            congrads.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
            progressValue.setVisibility(View.INVISIBLE);
            mProgress.setVisibility(View.VISIBLE);
        } else {
            instructions.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            startOverButton.setVisibility(View.INVISIBLE);
            congrads.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            progressValue.setVisibility(View.VISIBLE);
            progressValue.setText(Activities.activityPoints + " / " + Goal.goalPoints);
            mProgress.setVisibility(View.VISIBLE);
        }
    }

    //Set visibility on resume
    @Override
    public void onResume() {
        super.onResume();
        setProgressVisibility();
    }

    //Button action
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_over:
                startOver();
                break;
        }
    }
}
