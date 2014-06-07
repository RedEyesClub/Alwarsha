package com.alwarsha.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.DealProduct;
import com.alwarsha.app.Product;
import com.alwarsha.app.ProductGroup;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Farid on 5/31/14.
 */
public class DealsProvider {
    private static String TAG = "DealsProvider";

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private String[] mAllColumns = {

            DatabaseHelper.TABLE_DEAL_ID,DatabaseHelper.TABLE_DEAL_NAME, DatabaseHelper.TABLE_DEAL_OPEN_TIME,
            DatabaseHelper.TABLE_DEAL_CLOSE_TIME, DatabaseHelper.TABLE_DEAL_STATUS,
            DatabaseHelper.TABLE_DEAL_TOTAL,DatabaseHelper.TABLE_DEAL_DISCOUNT,
            DatabaseHelper.TABLE_DEAL_COMMENT};

    private DatabaseHelper mDbHelper;
    private Context mContext;

    // Singleton
    private static DealsProvider sInstace = null;

    public static DealsProvider getInstace(Context context) {
        if (sInstace == null) {
            sInstace = new DealsProvider(context);
        }
        return sInstace;
    }

    private DealsProvider(Context context) {
        super();
        mContext = context;
        mDbHelper = DatabaseHelper.getInstance(mContext);
    }

    public void close() {
        mDbHelper.close();
    }

    public int insertNewDeal(Deal deal) throws Exception {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(deal.getStatus() == Deal.DEAL_STATUS.OPEN)
        {
           if(getOpenDealByName(deal.getName()) != null)
           {
               Log.d(TAG, "Cannot insert new OPEN Deal becasue an Open Deal with the same name already exists");
               throw(new Exception("OPEN Deal with same name already exist in DB"));
           }
        }
        cv.put(DatabaseHelper.TABLE_DEAL_STATUS, deal.getStatus().toString());
        cv.put(DatabaseHelper.TABLE_DEAL_NAME, deal.getName());
        cv.put(DatabaseHelper.TABLE_DEAL_TOTAL, deal.getTotal());
        cv.put(DatabaseHelper.TABLE_DEAL_DISCOUNT, deal.getTotal_discount());
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        cv.put(DatabaseHelper.TABLE_DEAL_CLOSE_TIME, df.format(deal.getClose()));
        cv.put(DatabaseHelper.TABLE_DEAL_OPEN_TIME, df.format(deal.getOpen()));
        cv.put(DatabaseHelper.TABLE_DEAL_COMMENT, deal.getComment());

        long i  = db.insert(DatabaseHelper.TABLE_DEALS, null, cv);
        Log.d(TAG, "insert return value = " + i);

        //get last inserted deal
        SQLiteDatabase read_db ;
        Cursor cursor = null;
        try {
            read_db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_DEALS, mAllColumns,
                    null,null, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at insertNewDeal");
            return -1;
        }
        cursor.moveToLast();
        int last_deal_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_ID));

        ContentValues product_cv = new ContentValues();
        for(DealProduct dp: deal.getmProducts()){
            product_cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_PRODUCT_ID, dp.getmId());
            product_cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_STATUS, dp.getStatus().toString());
            product_cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_DEAL_ID, last_deal_id);

            i  = db.insert(DatabaseHelper.TABLE_DEALS_PRODUCT, null, product_cv);
            Log.d(TAG, "insert return value = " + i);
        }

        cursor.close();

        return last_deal_id;
    }

    public void deleteDeal(int deal_id)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long i  = db.delete(DatabaseHelper.TABLE_DEALS, "deal_id "+"=?", new String[]{String.valueOf(deal_id)});
        Log.d(TAG, "delete return value = " + i);
    }

    public void updateDeal(Deal deal)
    {
        SQLiteDatabase write_db = null;
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.TABLE_DEAL_ID, String.valueOf(deal.getId()));
        cv.put(DatabaseHelper.TABLE_DEAL_STATUS, deal.getStatus().toString());
        cv.put(DatabaseHelper.TABLE_DEAL_NAME, deal.getName());
        cv.put(DatabaseHelper.TABLE_DEAL_TOTAL, deal.getTotal());
        cv.put(DatabaseHelper.TABLE_DEAL_DISCOUNT, deal.getTotal_discount());
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        cv.put(DatabaseHelper.TABLE_DEAL_CLOSE_TIME, df.format(deal.getClose()));
        cv.put(DatabaseHelper.TABLE_DEAL_OPEN_TIME, df.format(deal.getOpen()));
        cv.put(DatabaseHelper.TABLE_DEAL_COMMENT, deal.getComment());

        write_db = mDbHelper.getWritableDatabase();
        long i  = write_db.update(DatabaseHelper.TABLE_DEALS, cv, "deal_id "+"="+String.valueOf(deal.getId()),null);
        Log.d(TAG, "insert return value = " + i);
    }

    public Deal getOpenDealByName(String name){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Deal open_deal = null;
        DealsProductProvider dpp = DealsProductProvider.getInstace(mContext);
        try {
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_DEALS, mAllColumns,
                    DatabaseHelper.TABLE_DEAL_NAME + "=? AND " + DatabaseHelper.TABLE_DEAL_STATUS + "=?"
                    ,new String[]{name, Deal.DEAL_STATUS.OPEN.toString()}, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at getOpenDealByName");
        }

        if(cursor.getCount() < 1)
        {
            cursor.close();
            return null;
        }

        if (cursor.moveToFirst()) {
            open_deal = new Deal();
            open_deal.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_ID)));
            open_deal.setName(name);
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            try{
                open_deal.setOpen(format.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_OPEN_TIME))));
                open_deal.setClose(format.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_CLOSE_TIME))));
            }
            catch(Exception ex){
                Log.d("getOpenDealByName","parsing date exception");
            }
            open_deal.setStatus(Deal.DEAL_STATUS.fromString(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_STATUS))));
            open_deal.setComment(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_COMMENT)));
            open_deal.setTotal(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_TOTAL))));
            open_deal.setTotal_discount(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_DISCOUNT))));

            open_deal.setmProducts(dpp.getDealProductsByDealId(open_deal.getId()));
        }
        cursor.close();
        return open_deal;
    }

    public List<Deal> getAllOpenDeals(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Deal open_deal = null;
        List<Deal> open_deals = new ArrayList<Deal>();

        try {
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_DEALS, mAllColumns,
                    DatabaseHelper.TABLE_DEAL_STATUS + "=?"
                    ,new String[]{ Deal.DEAL_STATUS.OPEN.toString()}, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at getOpenDealByName");
        }

        if (cursor.moveToFirst()) {
            for(int i=0; i< cursor.getCount(); i++){
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_NAME));
                open_deal = this.getOpenDealByName(name);
                if(open_deal != null){
                    open_deals.add(open_deal);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();

        return open_deals;
    }

    public List<Deal> getClosedDealsByName(String name){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Deal> closed_deals = new ArrayList<Deal>();
        DealsProductProvider dpp = DealsProductProvider.getInstace(mContext);


        try {
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_DEALS, mAllColumns,
                    DatabaseHelper.TABLE_DEAL_STATUS + "=? AND " + DatabaseHelper.TABLE_DEAL_NAME + "=?"
                    ,new String[]{ Deal.DEAL_STATUS.CLOSED.toString(),name}, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at getOpenDealByName");
        }

        if (cursor.moveToFirst()) {
            for(int i=0; i< cursor.getCount(); i++){
                Deal closed_deal = new Deal();
                closed_deal.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_ID)));
                closed_deal.setName(name);
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                try{
                    closed_deal.setOpen(format.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_OPEN_TIME))));
                    closed_deal.setClose(format.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_CLOSE_TIME))));
                }
                catch(Exception ex){
                    Log.d("getClosedDealsByName","parsing date exception");
                }
                closed_deal.setStatus(Deal.DEAL_STATUS.fromString(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_STATUS))));
                closed_deal.setComment(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_COMMENT)));
                closed_deal.setTotal(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_TOTAL))));
                closed_deal.setTotal_discount(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEAL_DISCOUNT))));
                closed_deal.setmProducts(dpp.getDealProductsByDealId(closed_deal.getId()));
                closed_deals.add(closed_deal);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return closed_deals;
    }


}
