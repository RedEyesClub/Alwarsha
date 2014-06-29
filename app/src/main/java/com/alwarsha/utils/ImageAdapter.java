package com.alwarsha.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.alwarsha.app.Deal;
import com.alwarsha.app.R;
import com.alwarsha.data.DealsProvider;

import 	android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Farid on 5/2/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public List<String> getmTablesPictures() {
        return mTablesPictures;
    }

    public void setmTablesPictures(List<String> mTablesPictures) {
        this.mTablesPictures = mTablesPictures;
    }

    private List<String> mTablesPictures = new ArrayList<String>();


    public ImageAdapter(Context c) {
        mContext = c;
        DealsProvider dp = DealsProvider.getInstace(c);
        List<Deal> open_deals = dp.getAllOpenDeals();
        for(Deal d: open_deals){
            mTablesPictures.add(d.getName());
        }
        for(int i =0; i<9; i++){
            boolean name_exist = false;
            for(String table_name: mTablesPictures){
                if(table_name.equals(String.valueOf(i))){
                    name_exist = true;
                }
            }
            if(name_exist == false){
                mTablesPictures.add(String.valueOf(i));
            }
        }
        java.util.Collections.sort(mTablesPictures);
    }

    public int getCount() {
        return mTablesPictures.size() + 3;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public String getDealName(int position){
        return mTablesPictures.get(position);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View returnedValue;
        returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_screen_item, null);
        TextView tableTextView = (TextView)returnedValue.findViewById(R.id.tableNameTextView);
        ImageView tableImage =(ImageView)returnedValue.findViewById(R.id.mainScreenOneItemImageView);

        if(position < mTablesPictures.size()){
            DealsProvider provider = DealsProvider.getInstace(mContext);
            Deal d  = provider.getOpenDealByName(mTablesPictures.get(position));
            tableTextView.setText("Table " + mTablesPictures.get(position));
            tableTextView.setBackgroundColor(0xffff00);
            if(d == null)
                tableImage.setImageResource(R.drawable.table);
            else
                tableImage.setImageResource(R.drawable.open_table);
        }else if (position == mTablesPictures.size()){
            tableTextView.setText("Add deal");
            tableTextView.setBackgroundColor(0xffff00);
            tableImage.setImageResource(mSpecialThumps[0]);
        }else if(position == mTablesPictures.size() + 1){
            tableTextView.setText("Menu");
            tableTextView.setBackgroundColor(0xffff00);
            tableImage.setImageResource(mSpecialThumps[1]);
        }else if(position == mTablesPictures.size() + 2){
            tableTextView.setText("Closed Deals");
            tableTextView.setBackgroundColor(0xffff00);
            tableImage.setImageResource(mSpecialThumps[2]);
        }

        return returnedValue;
    }

    public void addDeal(String dealName){

        mTablesPictures.add(dealName);
    }


    private Integer[] mSpecialThumps = {
            R.drawable.plus_add_green,
            R.drawable.menu,
            R.drawable.closed
    };
}
