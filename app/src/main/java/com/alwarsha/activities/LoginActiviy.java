package com.alwarsha.activities;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.R;
import com.alwarsha.app.StaffMember;
import com.alwarsha.data.StaffMembersProvider;
import com.alwarsha.utils.Utils;

public class LoginActiviy extends BaseActivity  {
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

        usernameEditText = (EditText)findViewById(R.id.username_editText);
        passwordEditText = (EditText)findViewById(R.id.password_editText);

        mProvider = StaffMembersProvider.getInstace(this);
        loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                AlwarshaApp.m = mProvider.getStaffMember(username,password);
                checkCredintials(username,password);
            }
        });
    }

    private void checkCredintials(final String username, final String password) {

        AlwarshaApp.m = mProvider.getStaffMember(username,password);
        if(AlwarshaApp.m == null){
            AlertDialog  errorAlert = new AlertDialog.Builder(this)
                    .setTitle("Error").setMessage("Bad username or password")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }else{
            AlertDialog  errorAlert = new AlertDialog.Builder(this)
                    .setTitle("True").setMessage("Welcome " + AlwarshaApp.m.getName() + " Ya "+ AlwarshaApp.m.getStatus())
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
