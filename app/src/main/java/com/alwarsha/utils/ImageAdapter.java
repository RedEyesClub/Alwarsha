package com.alwarsha.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.alwarsha.app.R;
import 	android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Farid on 5/2/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View returnedValue;
        returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_screen_item, null);
        TextView tableTextView = (TextView)returnedValue.findViewById(R.id.tableNameTextView);
        if(position == 9){
            tableTextView.setText("Menu");
        }else {
            tableTextView.setText("Table " + String.valueOf(position));
            tableTextView.setBackgroundColor(0xffff00);
        }
        ImageView tableImage =(ImageView)returnedValue.findViewById(R.id.mainScreenOneItemImageView);

        tableImage.setImageResource(mThumbIds[position]);
        return returnedValue;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.table, R.drawable.table,
            R.drawable.table, R.drawable.table,
            R.drawable.table, R.drawable.table,
            R.drawable.table, R.drawable.table,
            R.drawable.table, R.drawable.menu,
    };
}
