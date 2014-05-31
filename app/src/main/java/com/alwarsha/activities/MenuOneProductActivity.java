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
import com.alwarsha.data.ProductsGroupsProvider;
import com.alwarsha.data.ProductsProvider;
import com.alwarsha.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MenuOneProductActivity extends BaseActivity {

    public AlwarshaApp mApp;
    private static String TAG = "MenuOneProductActivity";
    private int mCategoryId;
    private String mSender;
    private String mDealNameId ="0";
    List<Product> mProductsList = new ArrayList<Product>();

    private BaseAdapter mAdapter = new BaseAdapter() {
        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSender != null){
                    TextView product = (TextView)v.findViewById(R.id.productId);
                    int position  = Integer.valueOf(product.getText().toString());

                    Product p = mProductsList.get(position);
                    DealProduct dp = new DealProduct(p, DealProduct.DealProductStatus.ORDERED);
                    mApp.getDealsList().get(Integer.valueOf(mDealNameId)).addProduct(dp);
                    Toast.makeText(MenuOneProductActivity.this,dp.getmName("EN") + " Added",Toast.LENGTH_SHORT).show();
                }
            }
        };
        @Override
        public int getCount() {
            return mProductsList.size();
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
            //TODO:Farid,  need to know wich language
            productName.setText(mProductsList.get(position).getmName("EN"));
            ImageView productImage =(ImageView)returnedValue.findViewById(R.id.productOneItemImageView);
            productImage.setImageBitmap(Utils.getBitmapFromStorage(mProductsList.get(position).getmPictureName()));
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
        if(mCategoryId > -1){
            ProductsProvider provider = ProductsProvider.getInstace(this);
            mProductsList = provider.getProductsCategory(String.valueOf(mCategoryId));
        }
        drinksListView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
