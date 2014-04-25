package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alwarsha.app.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void menuClick(View menuButton){
        Intent i = new Intent(MainActivity.this,MenuMainActivity.class);
        startActivity(i);
        finish();
    }

    public void table1Click(View table1Button){
        Intent i = new Intent(MainActivity.this,DealActivity.class);
        i.putExtra("dealName","Table_01");
        startActivity(i);
        finish();
    }

}
