package com.alwarsha.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.DealProduct;
import com.alwarsha.app.R;
import com.alwarsha.data.DealsProvider;
import com.alwarsha.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class ClosedDealsActivity extends Activity {

    ListView myList;
    List<String> listContent = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_deals);



        myList = (ListView)findViewById(R.id.activityClosedDealsDealsListView);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        final List<Deal> closedDeals = DealsProvider.getInstace(this).getAllClosedDeals();

        for(Deal d:closedDeals){
            listContent.add(sdf.format(d.getClose()) + "\t" + d.getName() + "\t" + d.getTotal());
        }

        ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this,

                android.R.layout.simple_list_item_1,

                listContent);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                sendCloseDeal(closedDeals.get(position));
            }
        });

        myList.setAdapter(adapter);


    }

    private void sendCloseDeal(Deal deal){
        if (deal== null || deal.getmProducts() == null || deal.getmProducts().size() == 0)
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        LinkedHashMap<Integer, Integer> sentProductsCounter = new LinkedHashMap<Integer, Integer>();
        float total =0;

        String dealClose = "+++++++++++++++++++++++++++++++" + '\r'  + '\n';
        dealClose += "Close deal" + '\r'  + '\n';
        dealClose += currentDateandTime + '\r' + '\n' + '\n';
        dealClose += "Table number : " + deal.getName() + '\r' + '\n';
        dealClose += AlwarshaApp.m.getName() + '\r' + '\n';
        ArrayList<String> printed = new ArrayList<String>();
        total =0;
        for (DealProduct d : deal.getmProducts()) {
            dealClose += d.getmName("EN") +  "  "  + d.getmPrice()  +" NIS"+ "\n";
            total+=d.getmPrice();
        }

        dealClose += "---- Total =  " + total + '\r' + '\n';
        dealClose += "---- Total after discount =  " + String.valueOf(total - total * deal.getTotal_discount() / 100) + '\r' + '\n';

        new sendToPrinterTask().execute(dealClose);
    }

    private class sendToPrinterTask extends
            AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                arg0[0] += "\n" + "\n" + "\n" + "\u001b"+"\u0069";
                int textLength = arg0[0].length();

                Socket client = new Socket("192.168.1.47", 9100);

                byte[] mybytearray = new byte[textLength];


                ByteArrayInputStream bufferedInputStream = new ByteArrayInputStream(arg0[0].getBytes());



                bufferedInputStream.read(mybytearray, 0, mybytearray.length);

                OutputStream outputStream = client.getOutputStream();

                outputStream.write(mybytearray, 0, mybytearray.length);
                outputStream.flush();
                bufferedInputStream.close();
                outputStream.close();
                client.close();


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
