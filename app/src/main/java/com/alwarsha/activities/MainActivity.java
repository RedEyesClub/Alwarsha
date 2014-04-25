package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

    public void tableClick(View tableButton){
        Intent i = new Intent(MainActivity.this,DealActivity.class);
        Button b = (Button)tableButton;
        String buttonText = b.getText().toString();
        i.putExtra("dealName",buttonText);
        startActivity(i);
        finish();
    }

}
