package com.alwarsha.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alwarsha.app.R;

public class DealActivity extends Activity {

    private String mDealNameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView deal_name;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mDealNameId = extras.getString("dealName");
        }

        deal_name = (TextView)findViewById(R.id.deal_name);
        deal_name.setText(mDealNameId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
