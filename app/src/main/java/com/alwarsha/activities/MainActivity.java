package com.alwarsha.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alwarsha.app.R;
import com.alwarsha.utils.ImageAdapter;

public class MainActivity extends BaseActivity {

    ImageAdapter mImageAdapter = new ImageAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridView);

        gridview.setAdapter(mImageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position < mImageAdapter.getmTablesPictures().size() ){
                    Intent i = new Intent(MainActivity.this,DealActivity.class);
                    // Button b = (Button)tableButton;
                    // String buttonText = b.getText().toString();
                    i.putExtra("dealName", String.valueOf(position));
                    startActivity(i);

                }else if(position == mImageAdapter.getmTablesPictures().size() ){
                    AlertDialog.Builder editalert = new AlertDialog.Builder(MainActivity.this);

                    editalert.setTitle("Insert new deal");
                    editalert.setMessage("Enter deal name");


                    final EditText input = new EditText(MainActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT);
                    input.setLayoutParams(lp);
                    input.setHeight(100);
                    editalert.setView(input);

                    editalert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mImageAdapter.addDeal(input.getText().toString());
                            mImageAdapter.notifyDataSetChanged();
                        }
                    });


                    editalert.show();
                }else if((position > mImageAdapter.getmTablesPictures().size() )){
                    Intent i = new Intent(MainActivity.this,MenuMainActivity.class);
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
