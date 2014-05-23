package com.alwarsha.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Product;
import com.alwarsha.app.ProductGroup;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samerm on 5/8/14.
 */
public class ProductsGroupsProvider {
    private static String TAG = "ProductsGroupsProvider";

    private String[] mAllColumns = {
            DatabaseHelper.TABLE_PRODUCT_GROUP_ID, DatabaseHelper.TABLE_PRODUCT_GROUP_NAME,
            DatabaseHelper.TABLE_PRODUCT_GROUP_NAME_AR, DatabaseHelper.TABLE_PRODUCT_GROUP_PIC_NAME};

    private DatabaseHelper mDbHelper;
    private Context mContext;

    @Root (name="ProductGroups")
    public static class ProductGroups{
        @ElementList (name="groups")
        public ArrayList<ProductGroup> groups;

        public ProductGroups()
        {
            groups = new ArrayList<ProductGroup>();
        }
    }

    // Singleton
    private static ProductsGroupsProvider sInstace = null;

    public static ProductsGroupsProvider getInstace(Context context) {
        if (sInstace == null) {
            sInstace = new ProductsGroupsProvider(context);
        }
        return sInstace;
    }

    private ProductsGroupsProvider(Context context) {
        super();
        mContext = context;
        mDbHelper = DatabaseHelper.getInstance(mContext);
    }

    public void close() {
        mDbHelper.close();
    }



    public void insertNewProductGroup(ProductGroup product_group) {
        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();

        Cursor cursor = db1.query(DatabaseHelper.TABLE_PRODUCTS_GROUPS, mAllColumns,
                DatabaseHelper.TABLE_PRODUCT_GROUP_ID + "= ?"  , new String[]{String.valueOf(product_group.getmId())}, null, null, null);
        if(cursor != null && cursor.getCount() != 0 ){
            return;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.TABLE_PRODUCT_GROUP_ID, product_group.getmId());
        String name_en = product_group.getmName("EN");
        if(name_en != null)
        {
            cv.put(DatabaseHelper.TABLE_PRODUCT_GROUP_NAME, product_group.getmName("EN"));
        }
        String name_ar = product_group.getmName("AR");
        if(name_ar != null)
        {
            cv.put(DatabaseHelper.TABLE_PRODUCT_GROUP_NAME_AR, product_group.getmName("AR"));
        }
        cv.put(DatabaseHelper.TABLE_PRODUCT_GROUP_PIC_NAME, product_group.getmPicName());

        long i  = db.insert(DatabaseHelper.TABLE_PRODUCTS_GROUPS, null, cv);
        Log.d(TAG, "insert return value = " + i);
    }

    public ProductGroup getProductGroup(int id) {

        ProductGroup product_group = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_PRODUCTS_GROUPS, mAllColumns,
                    DatabaseHelper.TABLE_PRODUCT_GROUP_ID + "=?"
                    ,new String[]{"1"}, null, null, null, null);
        }catch (NullPointerException e){
            if(AlwarshaApp.DEBUG)
                Log.e(TAG,"Exception at getProductGroup" );
        }
        if (cursor.moveToFirst()) {
            int db_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_ID));

            String db_name_ar = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_NAME_AR));

            String db_name_en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_NAME));

            String db_pic_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_PIC_NAME));
            cursor.close();

            product_group = new ProductGroup(db_id, db_name_en, db_pic_name,  "EN");
            product_group.addName("AR",db_name_ar);
        }
        return product_group;
    }

    public List<ProductGroup> ProductsGroups_getGroups(List<Integer> groups_ids)
    {
        List<ProductGroup> product_groups = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_PRODUCTS_GROUPS, mAllColumns,
                    null, null, null, null, null, null);
        }catch (NullPointerException e){
            if(AlwarshaApp.DEBUG)
                Log.e(TAG,"Exception at getProductGroup" );
        }
        if(cursor.moveToFirst())
        {
            do
            {
                ProductGroup new_group = null;
                int db_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_ID));

                if((groups_ids == null) || (groups_ids.contains(db_id)))
                {
                    String db_name_ar = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_NAME_AR));

                    String db_name_en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_NAME));

                    String db_pic_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_PIC_NAME));
                    cursor.close();

                    new_group = new ProductGroup(db_id, db_name_en, db_pic_name,  "EN");
                    new_group.addName("AR",db_name_ar);
                    product_groups.add(new_group);
                }
            }
            while(cursor.moveToNext());
        }

        return product_groups;
    }

    public void ProductsGroups_deleteDB()
    {
        SQLiteDatabase db = null;
        try{
            db = mDbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_PRODUCTS_GROUPS, null, null);

        }catch (NullPointerException e){
            if(AlwarshaApp.DEBUG)
                Log.e(TAG,"Exception at delete Products Groups Table" );
        }
    }
}
