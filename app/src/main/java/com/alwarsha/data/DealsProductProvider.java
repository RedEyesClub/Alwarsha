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

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samerm on 5/31/14.
 */
public class DealsProductProvider {
    private static String TAG = "DealsProductProvider";

    private String[] mAllColumns = {
            DatabaseHelper.TABLE_DEALS_PRODUCT_ID,
            DatabaseHelper.TABLE_DEALS_PRODUCT_PRODUCT_ID, DatabaseHelper.TABLE_DEALS_PRODUCT_STATUS,
            DatabaseHelper.TABLE_DEALS_PRODUCT_DEAL_ID};

    private DatabaseHelper mDbHelper;
    private Context mContext;

    // Singleton
    private static DealsProductProvider sInstace = null;

    public static DealsProductProvider getInstace(Context context) {
        if (sInstace == null) {
            sInstace = new DealsProductProvider(context);
        }
        return sInstace;
    }

    private DealsProductProvider(Context context) {
        super();
        mContext = context;
        mDbHelper = DatabaseHelper.getInstance(mContext);
    }

    public void close() {
        mDbHelper.close();
    }

    public int insertNewDealProduct(DealProduct deal_product) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try{
        cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_DEAL_ID, deal_product.getDeal_id());
        cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_PRODUCT_ID, deal_product.getmId());
        cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_STATUS, deal_product.getStatus().toString());
        }
        catch(Exception ex){
            Log.d(TAG, "exception");
        }
        long i  = db.insert(DatabaseHelper.TABLE_DEALS_PRODUCT, null, cv);
        Log.d(TAG, "insert return value = " + i);

        //get last inserted deal
        SQLiteDatabase read_db ;
        Cursor cursor = null;
        try {
            read_db = mDbHelper.getReadableDatabase();
            Log.d(TAG, "samer: before read_db.query");
            cursor = read_db.query(DatabaseHelper.TABLE_DEALS_PRODUCT, mAllColumns,
                    null,null, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at insertNewDeal");
            return -1;
        }
        cursor.moveToLast();
        int last_deal_product_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_DEALS_PRODUCT_ID));

        cursor.close();
        db.close();

        return last_deal_product_id;
    }

    public void deleteDealProduct(int deal_product_id)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long i  = db.delete(DatabaseHelper.TABLE_DEALS_PRODUCT, DatabaseHelper.TABLE_DEALS_PRODUCT_ID + "=?", new String[]{String.valueOf(deal_product_id)});
        Log.d(TAG, "delete return value = " + i);

        db.close();
    }

    public void updateDealProduct(DealProduct deal_product){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_DEAL_ID, deal_product.getDeal_id());
        cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_PRODUCT_ID, deal_product.getmId());
        cv.put(DatabaseHelper.TABLE_DEALS_PRODUCT_STATUS, deal_product.getStatus().toString());

        long i  = db.update(DatabaseHelper.TABLE_DEALS_PRODUCT, cv, DatabaseHelper.TABLE_DEALS_PRODUCT_ID +"=?",new String[]{String.valueOf(deal_product.getId())} );
        Log.d(TAG, "insert return value = " + i);

        db.close();
    }

    public List<DealProduct> getDealProductsByDealId(int deal_id){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ProductsProvider pp = ProductsProvider.getInstace(mContext);
        try {
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_DEALS_PRODUCT, mAllColumns,
                    DatabaseHelper.TABLE_DEALS_PRODUCT_DEAL_ID + "=?"
                    ,new String[]{String.valueOf(deal_id)}, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at getOpenDealByName");
        }

        if(cursor.getCount() == 0)
        {
            cursor.close();
            db.close();
            return null;
        }

        List<DealProduct> deal_products = new ArrayList<DealProduct>();
        if (cursor.moveToFirst()) {
            for(int i = 0; i<cursor.getCount(); i++){
                int deal_product_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_DEALS_PRODUCT_ID));
                int product_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_DEALS_PRODUCT_PRODUCT_ID));
                DealProduct.DealProductStatus status = DealProduct.DealProductStatus.fromString(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_DEALS_PRODUCT_STATUS)));
                Product p = pp.getProduct(product_id);
                if(p != null)
                {
                    DealProduct dp = new DealProduct(p,status,mContext);
                    dp.setDeal_id(deal_id);
                    dp.setId(deal_product_id);
                    deal_products.add(dp);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return deal_products;
    }
}
