package com.alwarsha.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.ProductCategory;
import com.alwarsha.app.R;
import com.alwarsha.data.ProductsCategoriesProvider;
import com.alwarsha.data.ProductsGroupsProvider;
import com.alwarsha.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MenuCategoriesActivity extends BaseActivity {

    public AlwarshaApp mApp;
    private static String TAG = "MenuCategoriesActivity";
    private String mDealNameId;
    private String mSender;
    private String mGroupId;
    private List<ProductCategory> categoriesList = new ArrayList<ProductCategory>();

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return categoriesList.size();
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


           categoryName.setText(categoriesList.get(position).getmName("EN"));
           ImageView categoryImage =(ImageView)returnedValue.findViewById(R.id.drinksOneItemImageView);
           categoryImage.setImageBitmap(Utils.getBitmapFromStorage(categoriesList.get(position).getmPicName()));
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
            mDealNameId = extras.getString("dealId");
            mGroupId = extras.getString("GroupId");
        }
        mApp =  AlwarshaApp.getInstance();

        ProductsCategoriesProvider provider = ProductsCategoriesProvider.getInstace(this);
        categoriesList = provider.getProductCategoriesGroup(mGroupId);
        ListView groupCategoriesListView = (ListView) findViewById(R.id.drinksListView);
        groupCategoriesListView.setAdapter(mAdapter);
        groupCategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MenuCategoriesActivity.this,MenuOneProductActivity.class);
                intent.putExtra("id",categoriesList.get(i).getmId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(mSender!=null){
                    intent.putExtra("sender", mSender);
                    intent.putExtra("dealId", mDealNameId);
                }
                startActivity(intent);
            }
        });
    }
}
