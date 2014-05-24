package com.alwarsha.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.ProductCategory;

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
public class ProductsCategoriesProvider {
    private static String TAG = "ProductsCategoriesProvider";

    private String[] mAllColumns = {
            DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID, DatabaseHelper.TABLE_PRODUCT_GROUP_ID, DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME,
            DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME_AR, DatabaseHelper.TABLE_PRODUCT_CATEGORY_PIC_NAME};

    private DatabaseHelper mDbHelper;
    private Context mContext;

    @Root(name = "ProductCategories")
    public static class ProductCategories {
        @ElementList(name = "categories")
        public ArrayList<ProductCategory> categories;

        public ProductCategories() {
            categories = new ArrayList<ProductCategory>();
        }
    }

    // Singleton
    private static ProductsCategoriesProvider sInstace = null;

    public static ProductsCategoriesProvider getInstace(Context context) {
        if (sInstace == null) {
            sInstace = new ProductsCategoriesProvider(context);
        }
        return sInstace;
    }

    private ProductsCategoriesProvider(Context context) {
        super();
        mContext = context;
        mDbHelper = DatabaseHelper.getInstance(mContext);
    }

    public void close() {
        mDbHelper.close();
    }


    public void insertNewProductCategory(ProductCategory product_cat) {
        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();

        Cursor cursor = db1.query(DatabaseHelper.TABLE_PRODUCTS_CATEGORIES, mAllColumns,
                DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID + "= ?", new String[]{String.valueOf(product_cat.getmId())}, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            return;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID, product_cat.getmId());
        String name_en = product_cat.getmName("EN");
        if (name_en != null) {
            cv.put(DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME, product_cat.getmName("EN"));
        }
        String name_ar = product_cat.getmName("AR");
        if (name_ar != null) {
            cv.put(DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME_AR, product_cat.getmName("AR"));
        }
        cv.put(DatabaseHelper.TABLE_PRODUCT_CATEGORY_PIC_NAME, product_cat.getmPicName());

        long i = db.insert(DatabaseHelper.TABLE_PRODUCTS_CATEGORIES, null, cv);
        Log.d(TAG, "insert return value = " + i);
    }

    public ProductCategory getProductCategory(String id) {

        ProductCategory product_cat = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_PRODUCTS_CATEGORIES, mAllColumns,
                    DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID + "=?"
                    , new String[]{id}, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at getProductGroup");
        }
        if (cursor.moveToFirst()) {
            int db_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID));

            int db_group_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_GROUP_ID));

            String db_name_ar = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME_AR));

            String db_name_en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME));

            String db_pic_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_PIC_NAME));
            cursor.close();

            product_cat = new ProductCategory(db_id, db_group_id, db_name_en, db_pic_name, "EN");
            product_cat.addName("AR", db_name_ar);

        }
        return product_cat;
    }

    public List<ProductCategory> getProductCategoriesGroup(String groupId) {

        List<ProductCategory> productCatList = new ArrayList<ProductCategory>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_PRODUCTS_CATEGORIES, mAllColumns,
                    DatabaseHelper.TABLE_PRODUCT_GROUP_ID + "=?"
                    ,new String[]{groupId}, null, null, null, null);
        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at getProductGroup");
        }
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                ProductCategory product_cat = null;
                int db_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_ID));

                int db_group_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_GROUP_ID));

                String db_name_ar = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME_AR));

                String db_name_en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_NAME));

                String db_pic_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PRODUCT_CATEGORY_PIC_NAME));

                product_cat = new ProductCategory(db_id, db_group_id, db_name_en, db_pic_name, "EN");
                product_cat.addName("AR", db_name_ar);
                productCatList.add(product_cat);

                cursor.moveToNext();

            }
        }
        cursor.close();
        return productCatList;
    }

    public void ProductsCategories_deleteDB() {
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_PRODUCTS_CATEGORIES, null, null);

        } catch (NullPointerException e) {
            if (AlwarshaApp.DEBUG)
                Log.e(TAG, "Exception at delete Products Categories Table");
        }
    }

    public boolean ProductsCategories_initDataBase(File xml_file) {
        if (xml_file.exists() == false) {
            return false;
        }

        Serializer serializer = new Persister();
        ProductCategories products_cats = new ProductCategories();
        try {
            products_cats = serializer.read(ProductCategories.class, xml_file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (products_cats == null) {
            return false;
        }

        ProductsCategories_deleteDB();

        for (ProductCategory pc : products_cats.categories) {
            insertNewProductCategory(pc);
        }
        return true;
    }

    public boolean updateProductsCategoriesDB(File products_cats) {
        if (products_cats == null) {
            return false;
        }

        if (ProductsCategories_initDataBase(products_cats) == false) {
            return false;
        }

        return true;
    }

}
