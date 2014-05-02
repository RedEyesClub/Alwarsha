package com.alwarsha.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.R;
import com.alwarsha.utils.Utils;



public class MenuDrinksActivity extends BaseActivity {

    public AlwarshaApp mApp;
    private static String TAG = "MenuDrinksActivity";
    private String mSender;

    private BaseAdapter mAdapter = new BaseAdapter() {

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
            return returnedValue;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drinks);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSender = extras.getString("sender");
        }
        mApp =  AlwarshaApp.getInstance();
        ListView drinksListView = (ListView) findViewById(R.id.drinksListView);
        drinksListView.setAdapter(mAdapter);
        drinksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MenuDrinksActivity.this,MenuOneProductActivity.class);
                intent.putExtra("id",i);
                if(mSender!=null)
                    intent.putExtra("sender", mSender);
                startActivity(intent);
            }
        });
    }
}
