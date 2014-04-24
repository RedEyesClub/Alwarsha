package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.R;
import com.alwarsha.utils.Utils;



public class MenuDrinksActivity extends Activity {

    public AlwarshaApp mApp;
    private static String TAG = "MenuDrinksActivity";

    private BaseAdapter mAdapter = new BaseAdapter() {
        private OnClickListener mOnButtonClicked = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuDrinksActivity.this,MenuOneProductActivity.class);
                i.putExtra("id",0);
                startActivity(i);
            }
        };
        @Override
        public int getCount() {
            return (mApp.getMenue().getmProductsCategory()).size();
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
            returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.drinks_item, null);
            TextView categoryName = (TextView)returnedValue.findViewById(R.id.drinksOneItemTextView);
            categoryName.setText((mApp.getMenue().getmProductsCategory()).get(position).getmName());
            Log.i(TAG, "categoryName = " + (mApp.getMenue().getmProductsCategory()).get(position).getmName());
            ImageView categoryImage =(ImageView)returnedValue.findViewById(R.id.drinksOneItemImageView);
            categoryImage.setImageBitmap(Utils.getBitmapFromStorage((mApp.getMenue().getmProductsCategory()).get(position).getmPicName()));
            returnedValue.setOnClickListener(mOnButtonClicked);
            return returnedValue;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drinks);
        mApp =  AlwarshaApp.getInstance();
        ListView drinksListView = (ListView) findViewById(R.id.drinksListView);
        drinksListView.setAdapter(mAdapter);
    }

}
