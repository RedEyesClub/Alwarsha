package com.alwarsha.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.DealProduct;
import com.alwarsha.app.Product;

import com.alwarsha.app.R;
import com.alwarsha.data.DealsProvider;
import com.alwarsha.data.ProductsProvider;
import com.alwarsha.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MenuOneProductActivity extends BaseActivity {

    public AlwarshaApp mApp;
    private static String TAG = "MenuOneProductActivity";
    private int mCategoryId;
    private String mSender;
    private String mDealName = "0";
    List<Product> mProductsList = new ArrayList<Product>();

    private BaseAdapter mAdapter = new BaseAdapter() {
        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSender != null) {
                    TextView product = (TextView) v.findViewById(R.id.productId);
                    int productId = Integer.valueOf(product.getText().toString());
                    Product p = mProductsList.get(productId);
                    Vibrator vibrate = (Vibrator) MenuOneProductActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    vibrate.vibrate(300);
                    v.playSoundEffect(SoundEffectConstants.CLICK);
                    DealProduct dp = new DealProduct(p, DealProduct.DealProductStatus.ORDERED,MenuOneProductActivity.this);
                    DealsProvider deals_provider = DealsProvider.getInstace(getApplicationContext());

                    Deal open_deal = deals_provider.getOpenDealByName(mDealName);
                    if (open_deal == null) {
                        return;
                    }

                    open_deal.addProduct(dp, getApplicationContext());

                    Toast.makeText(MenuOneProductActivity.this, dp.getmName("EN") + " Added", Toast.LENGTH_SHORT).show();
                }
            }
        };

        private View.OnLongClickListener mOnItemLongClicked = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder commentAlert = new AlertDialog.Builder(MenuOneProductActivity.this);
                final View v = view;
                commentAlert.setTitle("Product comment");
                commentAlert.setMessage("Enter comment");


                final EditText input = new EditText(MenuOneProductActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setHeight(100);
                commentAlert.setView(input);

                commentAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mSender != null) {
                            TextView product = (TextView) v.findViewById(R.id.productId);
                            int productId = Integer.valueOf(product.getText().toString());
                            Product p = mProductsList.get(productId);
                            DealProduct dp = new DealProduct(p, DealProduct.DealProductStatus.ORDERED,MenuOneProductActivity.this);
                            dp.setComment(input.getText().toString(),MenuOneProductActivity.this);
                            DealsProvider deals_provider = DealsProvider.getInstace(getApplicationContext());

                            Deal open_deal = deals_provider.getOpenDealByName(mDealName);
                            if (open_deal == null) {
                                return;
                            }

                            open_deal.addProduct(dp, getApplicationContext());
                            Toast.makeText(MenuOneProductActivity.this, dp.getmName("EN") + " Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                commentAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


                commentAlert.show();

                return false;
            }
        };

        @Override
        public int getCount() {
            return mProductsList.size() + 1;
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
            View returnedValue = null;
            if (position == mProductsList.size()) {
                returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
                TextView productPriceTextView = (TextView)returnedValue.findViewById(R.id.producrOneItemPriceLoTextView);
                productPriceTextView.setVisibility(View.GONE);
                TextView productName = (TextView) returnedValue.findViewById(R.id.producrOneItemNameTextView);
                //TODO:Farid,  need to know wich language
                productName.setText("Add new product");
                ImageView productImage = (ImageView) returnedValue.findViewById(R.id.productOneItemImageView);
                productImage.setImageDrawable(getResources().getDrawable(R.drawable.add_product));
                returnedValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMealOfTheDayDialog();
                    }
                });
            } else {
                returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
                TextView productIdTextView = (TextView) returnedValue.findViewById(R.id.productId);
                TextView productPriceTextView = (TextView)returnedValue.findViewById(R.id.producrOneItemPriceLoTextView);
                productIdTextView.setText(String.valueOf(position));
                productPriceTextView.setText(String.valueOf(mProductsList.get(position).getmPrice()) + " Shekel");
                TextView productName = (TextView) returnedValue.findViewById(R.id.producrOneItemNameTextView);
                //TODO:Farid,  need to know wich language
                productName.setText(mProductsList.get(position).getmName("EN"));
                ImageView productImage = (ImageView) returnedValue.findViewById(R.id.productOneItemImageView);
                productImage.setImageBitmap(Utils.getBitmapFromStorage(mProductsList.get(position).getmPictureName()));
                returnedValue.setOnClickListener(mOnButtonClicked);
                returnedValue.setOnLongClickListener(mOnItemLongClicked);

            }
            return returnedValue;
        }

    };

    private void showMealOfTheDayDialog(){
        AlertDialog.Builder commentAlert = new AlertDialog.Builder(MenuOneProductActivity.this);
        commentAlert.setTitle("Add product");
        commentAlert.setMessage("Enter new product and price");


        final EditText input = new EditText(MenuOneProductActivity.this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHeight(100);
        input.setHint("Deal name");
        layout.addView(input);

        final EditText input2 = new EditText(MenuOneProductActivity.this);
        input2.setLayoutParams(lp);
        input2.setHint("Price");
        input2.setHeight(100);
        layout.addView(input2);

        commentAlert.setView(layout);

        commentAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ProductsProvider p = ProductsProvider.getInstace(MenuOneProductActivity.this);

                Product product = new Product(mCategoryId * 1000 + mAdapter.getCount(),
                        input.getText().toString(),
                        mCategoryId,
                        input.getText().toString(),
                        Float.valueOf(input2.getText().toString()),
                        "EN");
                p.insertNewProduct(product);

                mAdapter.notifyDataSetChanged();
            }
        });

        commentAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        commentAlert.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_one_product);

    }

    @Override
    protected void onResume() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mCategoryId = extras.getInt("id");
            mSender = extras.getString("sender");
            mDealName = extras.getString("dealId");
        }


        mApp = AlwarshaApp.getInstance();
        ListView drinksListView = (ListView) findViewById(R.id.oneProductListView);
        if (mCategoryId > -1) {
            ProductsProvider provider = ProductsProvider.getInstace(this);
            mProductsList = provider.getProductsCategory(String.valueOf(mCategoryId));
        }
        drinksListView.setAdapter(mAdapter);
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void mainMenuClicked(View mainMenuButton){
        Intent i = new Intent(MenuOneProductActivity.this,MenuMainActivity.class);
        i.putExtra("dealId", mDealName);
        i.putExtra("sender",MenuOneProductActivity.class.getSimpleName());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void mainScreenClicked(View mainScreenButton){
        Intent i = new Intent(MenuOneProductActivity.this,MainActivity.class);
        i.putExtra("dealId", mDealName);
        i.putExtra("sender",MenuOneProductActivity.class.getSimpleName());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}
