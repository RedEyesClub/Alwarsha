package com.alwarsha.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Product;
import com.alwarsha.app.R;
import com.alwarsha.utils.Utils;

public class MenuOneProductActivity extends Activity {

    public AlwarshaApp mApp;
    private static String TAG = "MenuOneProductActivity";
    private int mCategoryId;
    private String mSender;

    private BaseAdapter mAdapter = new BaseAdapter() {
        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSender != null){
                    TextView product = (TextView)v.findViewById(R.id.productId);
                    int position  = Integer.valueOf(product.getText().toString());
                    Product p = (mApp.getMenue().getmProductsCategory()).get(mCategoryId).getmProductsList().get(position);
                    mApp.getDealsList().get(0).getmProducts().put(p, 1);
                }
            }
        };
        @Override
        public int getCount() {
            return (mApp.getMenue().getmProductsCategory()).get(mCategoryId).getmProductsList().size();
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
            TextView productIdTextView = (TextView)returnedValue.findViewById(R.id.productId);
            productIdTextView.setText(String.valueOf(position));
            TextView productName = (TextView)returnedValue.findViewById(R.id.producrOneItemNameTextView);
            productName.setText((mApp.getMenue().getmProductsCategory()).get(mCategoryId).getmProductsList().get(position).getmName());
            ImageView productImage =(ImageView)returnedValue.findViewById(R.id.productOneItemImageView);
            productImage.setImageBitmap(Utils.getBitmapFromStorage((mApp.getMenue().getmProductsCategory()).get(mCategoryId).getmProductsList().get(position).getmPictureName()));
            returnedValue.setOnClickListener(mOnButtonClicked);
            return returnedValue;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_one_product);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mCategoryId = extras.getInt("id");
            mSender = extras.getString("sender");
        }


        mApp =  AlwarshaApp.getInstance();
        ListView drinksListView = (ListView) findViewById(R.id.oneProductListView);
        drinksListView.setAdapter(mAdapter);
    }


}
