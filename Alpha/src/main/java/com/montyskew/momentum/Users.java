package com.montyskew.momentum;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.content.DialogInterface;


public class Users extends Fragment
        implements OnClickListener {

    private EditText newUser;
    public static String password;
    public static String username;
    private String origPwd;
    private String checkPwd;
    private String newPwd;
    private View view;

    //Create view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout
        view = inflater.inflate(R.layout.activity_users, container, false);
        //Assign widgets
        Button createUser = (Button) view.findViewById(R.id.create_user_button);
        Button delete = (Button) view.findViewById(R.id.delete_user);
        Button changePassword = (Button) view.findViewById(R.id.change_password);
        newUser = (EditText) view.findViewById(R.id.create_user_name);
        //Set listeners
        createUser.setOnClickListener(this);
        delete.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        //Set visibility
        setUsersVisibility();
        return view;
    }

    //Sets user visibility
    public void setUsersVisibility() {
        if (username == null) {
            EditText format = (EditText) view.findViewById(R.id.create_user_name);
            format.setText("");
            View createUser = view.findViewById(R.id.create_user);
            View createTable = view.findViewById(R.id.create_user_table);
            View user = view.findViewById(R.id.users);
            View table = view.findViewById(R.id.users_table);
            user.setVisibility(View.INVISIBLE);
            createUser.setVisibility(View.VISIBLE);
            table.setVisibility(View.INVISIBLE);
            createTable.setVisibility(View.VISIBLE);
        } else {
            View createUser = view.findViewById(R.id.create_user);
            View createTable = view.findViewById(R.id.create_user_table);
            View user = view.findViewById(R.id.users);
            View table = view.findViewById(R.id.users_table);
            TextView format = (TextView) view.findViewById(R.id.active_user);
            format.setText(username);
            user.setVisibility(View.VISIBLE);
            createUser.setVisibility(View.INVISIBLE);
            table.setVisibility(View.VISIBLE);
            createTable.setVisibility(View.INVISIBLE);
        }
    }

    //Create user dialog
    private void createUser() {
        final String userNew;
        assert (newUser.getText()) != null;
        userNew = (newUser.getText()).toString();
        //Check for empty input
        if (userNew.equals("")) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Username Is Empty")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        } else {
            final EditText pwd = new EditText(getActivity());
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            final AlertDialog.Builder check = new AlertDialog.Builder(getActivity());
            new AlertDialog.Builder(getActivity())
                    .setMessage("Enter Password")
                    .setView(pwd)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String pwdNew = (pwd.getText()).toString();
                            assert (pwd.getText()) != null;
                            //Check password
                            if (pwdNew.equals("")) {
                                check.setMessage("Password Empty");
                                check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        createUser();
                                    }
                                });
                                check.show();
                            } else {
                                //Add new user and password
                                username = userNew;
                                password = pwdNew;
                                //Update visibility
                                setUsersVisibility();
                                Goal.setGoalVisibility();
                            }
                        }
                    }).show();
        }
    }

    //Delete user dialog
    private void deleteUser() {
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
                checkPwd = (pwd.getText()).toString();
                //Check password
                if (checkPwd.equals(password)) {
                    username = null;
                    password = null;
                    setUsersVisibility();
                    Goal.setGoalVisibility();
                } else {
                    check.setMessage("Password Incorrect");
                    check.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteUser();
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

    //Change password dialog
    private void changePassword() {
        final EditText pwdCurrent = new EditText(getActivity());
        final EditText pwdNew = new EditText(getActivity());
        pwdCurrent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder pass = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder check = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder New = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder empty = new AlertDialog.Builder(getActivity());
        pass.setMessage("Old Password");
        pass.setView(pwdCurrent);
        pass.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                assert (pwdCurrent.getText()) != null;
                origPwd = (pwdCurrent.getText()).toString();
                //Check password
                if (origPwd.equals(password)) {
                    New.setMessage("New Password");
                    New.setView(pwdNew);
                    New.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            newPwd = (pwdNew.getText().toString());
                            assert newPwd != null;
                            //Check for empty input
                            if (newPwd.equals("")) {
                                empty.setMessage("Password Empty");
                                empty.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        changePassword();
                                    }
                                });
                                empty.show();
                            } else {
                                //Set new password
                                password = newPwd;
                                New.setMessage("Password Changed");
                            }
                        }
                    });
                    New.show();
                } else {
                    check.setMessage("Password Incorrect");
                    check.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            changePassword();
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

    //Bundle data on exit
    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putString("username", username);
        state.putString("password", password);
        super.onSaveInstanceState(state);
    }

    //Button actions
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_user_button:
                createUser();
                break;
            case R.id.delete_user:
                deleteUser();
                break;
            case R.id.change_password:
                changePassword();
                break;
        }
    }
}
