package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.Product;
import com.alwarsha.app.R;
import com.alwarsha.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealActivity extends BaseActivity {

    private String mDealNameId;
    private HashMap<Product,Integer> mProducts = new HashMap<Product, Integer>();
    private Deal deal;
    private int mTotal;
    private int mTotalDis;
    private AlwarshaApp mApp;

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
            productName.setText(((Product) (keys.get(position))).getmName());
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
        TextView totalTextView = (TextView)findViewById(R.id.totalTextView);
        TextView totalDisTextView = (TextView)findViewById(R.id.totalDisTextView);
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

        mProducts = deal.getmProducts();

        ListView mProductListView = (ListView) findViewById(R.id.deal_one_product_listView);
        mProductListView.setAdapter(mAdapter);

        for(Map.Entry<Product, Integer> entry : mProducts.entrySet()) {
            Product key = entry.getKey();
            Integer value = entry.getValue();
            total += key.getmPrice() * value;
        }

        totalTextView.setText("Total = " + total);
        totalDisTextView.setText("Total Dis = " +  total * 0.99);
        super.onResume();
    }

    public void add(View addButton){
        Intent i = new Intent(DealActivity.this,MenuMainActivity.class);
        i.putExtra("sender",DealActivity.class.getSimpleName());
        i.putExtra("dealId",mDealNameId);
        startActivity(i);
    }

    public void closeClicked(View closeButton){
        finish();
    }
}
