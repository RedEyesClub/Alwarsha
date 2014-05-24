package com.alwarsha.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Product;
import com.alwarsha.app.ProductCategory;
import com.alwarsha.app.ProductGroup;
import com.alwarsha.app.StaffMember;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farid on 4/20/14.
 */

public class ProductsProvider {

    private static String TAG = "ProductsProvider";

    private String[] mAllColumns = {
            DatabaseHelper.TABLE_PRODUCT_ID, DatabaseHelper.TABLE_PRODUCT_NAME,
            DatabaseHelper.TABLE_PRODUCT_NAME_AR, DatabaseHelper.TABLE_PRODUCT_PRICE,
            DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID,DatabaseHelper.TABLE_PRODUCT_PIC_NAME,
            DatabaseHelper.TABLE_PRODUCT_CAT_ID};

    private DatabaseHelper mDbHelper;
    private Context mContext;

    @Root(name="Products")
    public static class Products{
        @ElementList(name="products")
        public ArrayList<Product> products;

        public Products()
        {
            products = new ArrayList<Product>();
        }
    }

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
    private static ProductsProvider sInstace = null;

    public static ProductsProvider getInstace(Context context) {
        if (sInstace == null) {
            sInstace = new ProductsProvider(context);
        }
        return sInstace;
    }

    private ProductsProvider(Context context) {
        super();
        mContext = context;
        mDbHelper = DatabaseHelper.getInstance(mContext);
    }

    public void close() {
        mDbHelper.close();
    }



    public void insertNewProduct(Product product) {
		SQLiteDatabase db1 = mDbHelper.getReadableDatabase();

		Cursor cursor = db1.query(DatabaseHelper.TABLE_PRODUCTS, mAllColumns,
				DatabaseHelper.TABLE_PRODUCT_ID + "= ?"  , new String[]{String.valueOf(product.getmId())}, null, null, null);
		if(cursor != null && cursor.getCount() != 0 ){
			return;
		}

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.TABLE_PRODUCT_ID, product.getmId());
        String name_en = product.getmName("EN");
        if(name_en != null)
        {
            cv.put(DatabaseHelper.TABLE_PRODUCT_NAME, product.getmName("EN"));
        }
        String name_ar = product.getmName("AR");
        if(name_ar != null)
        {
            cv.put(DatabaseHelper.TABLE_PRODUCT_NAME_AR, product.getmName("AR"));
        }
        cv.put(DatabaseHelper.TABLE_PRODUCT_NAME, product.getmName("AR"));
        cv.put(DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID, product.getmCategoryId());
        cv.put(DatabaseHelper.TABLE_PRODUCT_PIC_NAME, product.getmPictureName());
        cv.put(DatabaseHelper.TABLE_PRODUCT_PRICE, product.getmPrice());

        long i  = db.insert(DatabaseHelper.TABLE_PRODUCTS, null, cv);
        Log.d(TAG, "insert return value = " + i);
    }
    public List<Product> getProductsCategory(String categoryId){
        List<Product> list = new ArrayList<Product>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, mAllColumns,
                    DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID + "=?"
                    ,new String[]{categoryId}, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at getProductGroup");
        }
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Product product = null;
                int db_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_ID));
                String db_name_ar = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME_AR));

                String db_name_en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME));

                int db_category_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CAT_ID));
                String db_pic_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PIC_NAME));
                float db_price = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PRICE));

                product = new Product(db_id, db_name_en, db_category_id, db_pic_name,  db_price, "EN");
                product.addName("AR", db_name_ar);
                list.add(product);

                cursor.moveToNext();

            }
        }
        cursor.close();

        return list;
    }
    public Product getProduct(String id) {

        Product product = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, mAllColumns,
                    DatabaseHelper.TABLE_PRODUCT_ID + "=?"
                    ,new String[]{id}, null, null, null, null);
        }catch (NullPointerException e){
            if(AlwarshaApp.DEBUG)
                Log.e(TAG,"Exception at getProduct" );
        }
        if (cursor.moveToFirst()) {
            int db_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_ID));
            String db_name_ar = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME_AR));

            String db_name_en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_NAME));

            int db_category_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CAT_ID));
            String db_pic_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PIC_NAME));
            float db_price = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_PRICE));
            cursor.close();

            product = new Product(db_id, db_name_en, db_category_id, db_pic_name,  db_price, "EN");
            product.addName("AR", db_name_ar);
        }
        return product;
    }

    public void Products_deleteDB()
    {
        SQLiteDatabase db = null;
        try{
            db = mDbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_PRODUCTS, null, null);

        }catch (NullPointerException e){
            if(AlwarshaApp.DEBUG)
                Log.e(TAG,"Exception at delete Products Table" );
        }
    }

    public boolean Products_initDataBase(File xml_file)
    {
        if (xml_file.exists() == false) {
            return false;
        }

        Serializer serializer = new Persister();
        Products products = new Products();
        try {
            products = serializer.read(Products.class, xml_file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(products == null)
        {
            return false;
        }

        Products_deleteDB();

        for(Product p : products.products)
        {
            insertNewProduct(p);
        }
        return true;
    }

    public boolean updateSellProductsDB(File sell_products)
    {
        if(sell_products == null)
        {
            return false;
        }

        if(Products_initDataBase(sell_products) == false)
        {
            return false;
        }

        return true;
    }
}
