package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alwarsha.app.R;

public class MenuMainActivity extends Activity {

    private String mSender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSender = extras.getString("sender");
        }
    }

    public void drinksClick(View drinksButton){
        Intent i = new Intent(MenuMainActivity.this,MenuDrinksActivity.class);
        if(mSender != null)
            i.putExtra("sender",mSender);
        startActivity(i);
        finish();
    }
}
