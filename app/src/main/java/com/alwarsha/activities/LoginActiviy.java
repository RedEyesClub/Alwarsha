package com.alwarsha.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alwarsha.app.R;
import com.alwarsha.app.StaffMember;
import com.alwarsha.data.StaffMembersProvider;
import com.alwarsha.utils.Utils;

public class LoginActiviy extends Activity {
    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username;
    private String password;
    private StaffMembersProvider mProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activiy);

        StaffMember member = new StaffMember("kalb","saher","xxx");

        mProvider = StaffMembersProvider.getInstace(this);
     //   mProvider.insertNewStaffMember(member);



        usernameEditText = (EditText)findViewById(R.id.username_editText);
        passwordEditText = (EditText)findViewById(R.id.password_editText);

        loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                checkCredintials(username,password);

            }
        });
        StaffMembersProvider mStaffMembersProvider = StaffMembersProvider.getInstace(this);
        if(mStaffMembersProvider.StaffMemeber_initDataBase(Utils.getFileFromStorage("Staff.xml")) == false)
        {
            AlertDialog errorAlert = new AlertDialog.Builder(this)
                    .setTitle("Error").setMessage("Staff Members XML")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private void checkCredintials(final String username, final String password) {

        StaffMember m = mProvider.getStaffMember(username,password);
        if(m == null){
            AlertDialog  errorAlert = new AlertDialog.Builder(this)
                    .setTitle("Error").setMessage("Bad username or password")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }else{
            AlertDialog  errorAlert = new AlertDialog.Builder(this)
                    .setTitle("True").setMessage("Welcome " + m.getName() + " Ya "+ m.getStatus())
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(LoginActiviy.this,MainActivity.class);
                            startActivity(i);

                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }
    }

}
