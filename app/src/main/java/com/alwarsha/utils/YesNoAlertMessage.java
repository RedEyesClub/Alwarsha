package com.alwarsha.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alwarsha.app.R;

/**
 * Created by Farid on 6/21/14.
 */
public class YesNoAlertMessage  extends AlertDialog {
    public Activity c;
    public Dialog d;
    public Button yes, no;
    public View space;
    private String mTitle;
    private TextView mTitleTextView;
    private String mMessage;



    public YesNoAlertMessage(Activity a,String title,String message,final View v) {
        super(a);
        this.c = a;
        this.mTitle = title;
        this.mMessage = message;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yes_no_message_dialog);

        space = findViewById(R.id.space);

        mTitleTextView = (TextView)findViewById(R.id.yesNoMessageDialogTiltleTextView);
        mTitleTextView.setText(mTitle);


        mTitleTextView = (TextView)findViewById(R.id.yesNoMessageDialogMessage);
        mTitleTextView.setText(mMessage);

        yes = (Button) findViewById(R.id.yesNoDialogYesButton);
        no = (Button) findViewById(R.id.yesNoDialogNoButton);
    }
}
