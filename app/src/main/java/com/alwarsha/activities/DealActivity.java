package com.alwarsha.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.DealProduct;
import com.alwarsha.app.Product;
import com.alwarsha.app.R;
import com.alwarsha.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealActivity extends BaseActivity {

    private String mDealNameId;
    private HashMap<DealProduct,Integer> mProducts = new HashMap<DealProduct, Integer>();
    private Deal deal;
    private int mTotal;
    private int mTotalDis;
    private AlwarshaApp mApp;
    private Socket client;
    private FileInputStream fileInputStream;
    private ByteArrayInputStream bufferedInputStream;
    private OutputStream outputStream;
    private String mOrdersToSend = "";
    TextView mTotalTextView;
    TextView mTotalDisTextView;
    ListView mProductListView;

    private BaseAdapter mAdapter = new BaseAdapter() {
        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        @Override
        public int getCount() {
            return (mProducts.size());
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View returnedValue;
            returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
            TextView productName = (TextView)returnedValue.findViewById(R.id.producrOneItemNameTextView);
            List keys = new ArrayList(mProducts.keySet());
            productName.setText(((Product) (keys.get(position))).getName());
            ImageView productImage =(ImageView)returnedValue.findViewById(R.id.productOneItemImageView);
            productImage.setImageBitmap(Utils.getBitmapFromStorage(((Product)(keys.get(position))).getmPictureName()));
            TextView count = (TextView)returnedValue.findViewById(R.id.countTextView);
            count.setText(" " + (int)mProducts.get(((Product)(keys.get(position)))));
            return returnedValue;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView deal_name;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        mTotalTextView = (TextView)findViewById(R.id.totalTextView);
        mTotalDisTextView = (TextView)findViewById(R.id.totalDisTextView);
        mProductListView = (ListView) findViewById(R.id.deal_one_product_listView);

        mApp = AlwarshaApp.getInstance();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mDealNameId = extras.getString("dealName");
        }

        deal_name = (TextView)findViewById(R.id.deal_name);
        deal_name.setText(mDealNameId);

    }

    @Override
    protected void onResume() {

        int total = 0;

        for(Deal l : mApp.getDealsList() ){
            if(l.getName().equals(mDealNameId)){
                deal = new Deal(l);
                mProducts = l.getmProducts();
                break;
            }
        }
        if(deal == null){
            deal = new Deal(mDealNameId,new Date());
            mApp.getDealsList().add(deal);
        }

        if(deal.getmProducts() != null && deal.getmProducts().size() > 0){

        mProducts = deal.getmProducts();


        mProductListView.setAdapter(mAdapter);

        for(Map.Entry<DealProduct, Integer> entry : mProducts.entrySet()) {
            Product key = entry.getKey();
            Integer value = entry.getValue();
            total += key.getmPrice() * value;
        }
        }

        mTotalTextView.setText("Total = " + total);
        mTotalDisTextView.setText("Total Dis = " +  total * 0.99);
        super.onResume();

    }

    public void add(View addButton){
        Intent i = new Intent(DealActivity.this,MenuMainActivity.class);
        i.putExtra("sender",DealActivity.class.getSimpleName());
        i.putExtra("dealId",mDealNameId);
        startActivity(i);
    }

    public void send(View sendButton){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        String currentDateandTime = sdf.format(new Date());

        mOrdersToSend = currentDateandTime +  '\r' + '\n' + '\n';
        mOrdersToSend += "Table number : " + mDealNameId + '\r' + '\n';
        mOrdersToSend += AlwarshaApp.m.getName() + '\r' + '\n';
        for(Map.Entry<DealProduct, Integer> entry : mProducts.entrySet()) {
            DealProduct key = entry.getKey();
            Integer value = entry.getValue();
            if(key.getStatus() == DealProduct.DealProductStatus.ORDERED){
                mOrdersToSend += key.getName()+ '\t' + value + '\r' + '\n';
            }
        }
        new Task().execute("ff");
    }


    public void closeClicked(View closeButton){
        finish();
    }
    private class Task extends
            AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.d("fdsa","fadf");
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try
            {
                int textLength = mOrdersToSend.length();

                client = new Socket("192.168.1.19", 9100);

                byte[] mybytearray = new byte[textLength];


                bufferedInputStream = new ByteArrayInputStream(mOrdersToSend.getBytes());

                bufferedInputStream.read(mybytearray, 0, mybytearray.length);

                outputStream = client.getOutputStream();

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
