package com.alwarsha.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alwarsha.app.R;

/**
 * Created by Farid on 6/24/14.
 */
public class ErrorAlert extends Dialog implements
        android.view.View.OnClickListener{

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private String mTitle;
    private String mMessage;
    private TextView mTitleTextView;
    private TextView mMessageTextView;

    public ErrorAlert(Activity a,String title,String message) {
        super(a);
        this.c = a;
        this.mTitle = title;
        this.mMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.error_dialog);

        mTitleTextView = (TextView)findViewById(R.id.errorDialogTiltleTextView);
        mMessageTextView = (TextView)findViewById(R.id.errorDialogMessageTextView);

        mTitleTextView.setText(mTitle);
        mMessageTextView.setText(mMessage);

        yes = (Button) findViewById(R.id.errorDialogOkButton);
        yes.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
        dismiss();
        }
    }

}
