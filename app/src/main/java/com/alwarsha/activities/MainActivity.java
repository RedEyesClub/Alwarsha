package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.alwarsha.app.R;
import com.alwarsha.utils.ImageAdapter;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position == 9){
                    Intent i = new Intent(MainActivity.this,MenuMainActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(MainActivity.this,DealActivity.class);
                    // Button b = (Button)tableButton;
                    // String buttonText = b.getText().toString();
                    i.putExtra("dealName"," " + position);
                    startActivity(i);
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
