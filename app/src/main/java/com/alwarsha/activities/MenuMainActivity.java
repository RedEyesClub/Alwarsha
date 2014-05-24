package com.alwarsha.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.ProductGroup;
import com.alwarsha.app.R;
import com.alwarsha.data.ProductsGroupsProvider;
import com.alwarsha.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MenuMainActivity extends BaseActivity {

    private String mSender;
    private String mDealNameId;
    private String TAG = "MenuMainActivity";
    private List<ProductGroup> listOfGroups = new ArrayList<ProductGroup>();
    private ListView mProductsGroupsListView;

    private BaseAdapter mProductsGroupsAdapter = new BaseAdapter() {

       private View.OnClickListener clickListener = new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               TextView groupIdTextView = (TextView)(v.findViewById(R.id.groupIdTextView));
               String groupId = groupIdTextView.getText().toString();
               Intent i = new Intent(MenuMainActivity.this,MenuCategoriesActivity.class);
               if(mSender != null){
                   i.putExtra("sender",mSender);
                   i.putExtra("dealId",mDealNameId);
               }
               i.putExtra("GroupId",groupId);
               startActivity(i);

           }
       };

        @Override
        public int getCount() {
            return listOfGroups.size();
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
            returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_groups_item, null);
            TextView groupName = (TextView)returnedValue.findViewById(R.id.textView);
            TextView groupId = (TextView)returnedValue.findViewById(R.id.groupIdTextView);
            //TODO: Farid, need to passe the language as parameter.
            groupName.setText(listOfGroups.get(position).getmName("EN"));
            groupId.setText(String.valueOf(listOfGroups.get(position).getmId()));
            ImageView groupImage =(ImageView)returnedValue.findViewById(R.id.imageView);
            returnedValue.setOnClickListener(clickListener);
            groupImage.setImageBitmap(Utils.getBitmapFromStorage(listOfGroups.get(position).getmPicName()));
            return returnedValue;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSender = extras.getString("sender");
            mDealNameId = extras.getString("dealId");
        }

        ProductsGroupsProvider provider = ProductsGroupsProvider.getInstace(this);
        listOfGroups = provider.ProductsGroups_getGroups(null);

        mProductsGroupsListView = (ListView)findViewById(R.id.productsGroupsListView);
        mProductsGroupsListView.setAdapter(mProductsGroupsAdapter);


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
