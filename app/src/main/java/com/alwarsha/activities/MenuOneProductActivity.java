package com.alwarsha.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.DealProduct;
import com.alwarsha.app.Product;
import com.alwarsha.app.R;
import com.alwarsha.utils.Utils;

public class MenuOneProductActivity extends BaseActivity {

    public AlwarshaApp mApp;
    private static String TAG = "MenuOneProductActivity";
    private int mCategoryId;
    private String mSender;
    private String mDealNameId ="0";

    private BaseAdapter mAdapter = new BaseAdapter() {
        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSender != null){
                    TextView product = (TextView)v.findViewById(R.id.productId);
                    int position  = Integer.valueOf(product.getText().toString());
                    Product p = (mApp.getMenue().getmProductsCategory()).get(mCategoryId).getmProductsList().get(position);
                    DealProduct dp = new DealProduct(p, DealProduct.DealProductStatus.ORDERED);
                    mApp.getDealsList().get(Integer.valueOf(mDealNameId)).addProduct(dp);
                    Toast.makeText(MenuOneProductActivity.this,dp.getName() + " Added",Toast.LENGTH_SHORT).show();
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
            productName.setText((mApp.getMenue().getmProductsCategory()).get(mCategoryId).getmProductsList().get(position).getName());
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
            mDealNameId = extras.getString("dealId");
        }


        mApp =  AlwarshaApp.getInstance();
        ListView drinksListView = (ListView) findViewById(R.id.oneProductListView);
        drinksListView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
