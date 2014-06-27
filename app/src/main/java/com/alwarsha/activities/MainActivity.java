package com.alwarsha.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.R;
import com.alwarsha.data.DealsProvider;
import com.alwarsha.utils.ErrorAlert;
import com.alwarsha.utils.ImageAdapter;
import com.alwarsha.utils.YesNoAlertMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class MainActivity extends BaseActivity {

    ImageAdapter mImageAdapter = new ImageAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridView);

        showMealOfTheDayDialog();
        gridview.setAdapter(mImageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position < mImageAdapter.getmTablesPictures().size() ){
                    Intent i = new Intent(MainActivity.this,DealActivity.class);
                    i.putExtra("dealName", mImageAdapter.getDealName(position));
                    startActivity(i);

                }else if(position == mImageAdapter.getmTablesPictures().size() ){
                    AlertDialog.Builder editalert = new AlertDialog.Builder(MainActivity.this);

                    editalert.setTitle("Insert new deal");
                    editalert.setMessage("Enter deal name");


                    final EditText input = new EditText(MainActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setHeight(100);
                    editalert.setView(input);

                    editalert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String deal_name = input.getText().toString();
                            if(deal_name.substring(0,1).matches("[0-9]")){
                                final AlertDialog.Builder BadNameAlert = new AlertDialog.Builder(MainActivity.this);
                                BadNameAlert.setTitle("Bad Deal Name!");
                                BadNameAlert.setMessage("Deal Name cannot start with digit");
                                BadNameAlert.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                            else{
                                mImageAdapter.addDeal(deal_name);
                                mImageAdapter.notifyDataSetChanged();
                            }
                        }
                    });


                    editalert.show();
                }else if((position == mImageAdapter.getmTablesPictures().size() + 1)){
                    Intent i = new Intent(MainActivity.this,MenuMainActivity.class);
                    startActivity(i);
                }else if((position == mImageAdapter.getmTablesPictures().size() + 2)){
                       Intent i= new Intent(MainActivity.this,ClosedDealsActivity.class);
                       startActivity(i);
                }

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showMealOfTheDayDialog(){
        AlertDialog.Builder commentAlert = new AlertDialog.Builder(MainActivity.this);
        commentAlert.setTitle("Deal Of The Day");
        commentAlert.setMessage("Enter the deal of the day in the price");


        final EditText input = new EditText(MainActivity.this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //lp.setOrientation(LinearLayout.VERTICAL);
        input.setLayoutParams(lp);
        input.setHeight(100);
        input.setHint("Deal name");
        layout.addView(input);

        final EditText input2 = new EditText(MainActivity.this);
        input2.setLayoutParams(lp);
        input2.setHint("Price");
        input2.setHeight(100);
        layout.addView(input2);

        commentAlert.setView(layout);

        commentAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        commentAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        commentAlert.show();
    }

    private void sendDataBaseByMail(){
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Alwarsha - User:" + AlwarshaApp.m.getName());
        //sendIntent.putExtra(Intent.EXTRA_TEXT,"Body of email");
        File file = new File("/data/data/com.alwarsha.app/databases/alwarsha.db");
        File backupfile = new File(
                Environment.getExternalStorageDirectory()
                        + "/MyDir/alwarsha.txt");
        try {
            FileChannel src = new FileInputStream(file).getChannel();
            FileChannel dst = new FileOutputStream(backupfile).getChannel();
            try {
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


     //   Uri uri = Uri.fromFile(getDatabasePath("alwarsha.db"));
        Uri uri = Uri.fromFile(backupfile);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, "samer.mattar.85@gmail.com");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("application/octet-stream");

        startActivity(Intent.createChooser(sendIntent,"Email:"));
    }

    @Override
    public void onBackPressed() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                final YesNoAlertMessage error = new YesNoAlertMessage(
                        MainActivity.this, "Exit", "Are you sure?", null);
                error.show();

                error.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Deal> deals = DealsProvider.getInstace(MainActivity.this).getAllOpenDeals();
                        if(deals.size() > 0 ){
                            String openDeals ="";
                            for(Deal d:deals){
                                openDeals += d.getName() + ",";
                            }
                            ErrorAlert error = new ErrorAlert(MainActivity.this,"Open deals","Please close all deals : " + openDeals);
                            error.show();
                        }else{
                            sendDataBaseByMail();
                            finish();
                        }
                       // finish();
                    }
                });
                error.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        error.dismiss();
                    }
                });
            }
        });

    }
}
