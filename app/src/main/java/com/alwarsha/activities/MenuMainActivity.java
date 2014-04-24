package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alwarsha.app.R;

public class MenuMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
    }

    public void drinksClick(View drinksButton){
        Intent i = new Intent(MenuMainActivity.this,MenuDrinksActivity.class);
        startActivity(i);
        finish();
    }
}
