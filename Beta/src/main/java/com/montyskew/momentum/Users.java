package com.montyskew.momentum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.widget.Toast;


public class Users extends Activity
        implements OnEditorActionListener,
        OnClickListener {

    private EditText newUser;
    private String password;
    private String username;
    private String origPwd;
    private String checkPwd;
    private String newPwd;
    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //set users visibility
        setUsersVisibility();

        //reference widgets
        Button createUser = (Button) findViewById(R.id.create_user_button);
        Button delete = (Button) findViewById(R.id.delete_user);
        Button changePassword = (Button) findViewById(R.id.change_password);
        newUser = (EditText) findViewById(R.id.create_user_name);

        //set listeners
        createUser.setOnClickListener(this);
        delete.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        newUser.setOnEditorActionListener(this);

        //Shared Preferences
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    public void setUsersVisibility() {
        if (username == null) {
            EditText format = (EditText) findViewById(R.id.create_user_name);
            format.setText("");
            View createUser = findViewById(R.id.create_user);
            View createTable = findViewById(R.id.create_user_table);
            View user = findViewById(R.id.users);
            View table = findViewById(R.id.users_table);
            user.setVisibility(View.INVISIBLE);
            createUser.setVisibility(View.VISIBLE);
            table.setVisibility(View.INVISIBLE);
            createTable.setVisibility(View.VISIBLE);
        } else {
            View createUser = findViewById(R.id.create_user);
            View createTable = findViewById(R.id.create_user_table);
            View user = findViewById(R.id.users);
            View table = findViewById(R.id.users_table);
            user.setVisibility(View.VISIBLE);
            createUser.setVisibility(View.INVISIBLE);
            table.setVisibility(View.VISIBLE);
            createTable.setVisibility(View.INVISIBLE);
        }
    }

    private void createUser() {
        final String userNew;
        assert (newUser.getText()) != null;
        userNew = (newUser.getText()).toString();
        if (userNew.equals("")) {
            new AlertDialog.Builder(this)
                    .setMessage("Username Is Empty")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        } else {
            final EditText pwd = new EditText(this);
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            final AlertDialog.Builder check = new AlertDialog.Builder(this);
            new AlertDialog.Builder(this)
                    .setMessage("Enter Password")
                    .setView(pwd)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String pwdNew = (pwd.getText()).toString();
                            assert (pwd.getText()) != null;
                            if (pwdNew.equals("")) {
                                check.setMessage("Password Empty");
                                check.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        createUser();
                                    }
                                });
                                check.show();
                            } else {
                                username = userNew;
                                password = pwdNew;
                                TextView format = (TextView) findViewById(R.id.active_user);
                                format.setText(username);
                                setUsersVisibility();
                            }
                        }
                    }).show();
        }
    }

    private void deleteUser() {
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
                checkPwd = (pwd.getText()).toString();
                if (checkPwd.equals(password)) {
                    String toastUser = username;
                    username = null;
                    password = null;
                    setUsersVisibility();
                    Toast.makeText(getApplicationContext(), "Deleted " + toastUser, Toast.LENGTH_LONG).show();
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

    private void changePassword() {
        final EditText pwdCurrent = new EditText(this);
        final EditText pwdNew = new EditText(this);
        pwdCurrent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder pass = new AlertDialog.Builder(this);
        final AlertDialog.Builder check = new AlertDialog.Builder(this);
        final AlertDialog.Builder New = new AlertDialog.Builder(this);
        final AlertDialog.Builder empty = new AlertDialog.Builder(this);
        pass.setMessage("Old Password");
        pass.setView(pwdCurrent);
        pass.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                assert (pwdCurrent.getText()) != null;
                origPwd = (pwdCurrent.getText()).toString();
                if (origPwd.equals(password)) {
                    New.setMessage("New Password");
                    New.setView(pwdNew);
                    New.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            newPwd = (pwdNew.getText().toString());
                            assert newPwd != null;
                            if (newPwd.equals("")) {
                                empty.setMessage("Password Empty");
                                empty.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        changePassword();
                                    }
                                });
                                empty.show();
                            }
                            else {
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

    @Override
    public void onPause() {
        //save variables
        Editor editor = savedValues.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Resume Variables
        username = savedValues.getString("username", "");
        password = savedValues.getString("password", "");
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            createUser();
        }
        return false;
    }

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

}
