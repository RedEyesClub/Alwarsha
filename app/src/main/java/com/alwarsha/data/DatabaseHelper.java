package com.alwarsha.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper mInstance = null;

	private static final String DB_NAME = "alwarsha.db";
	private static final int SCHEMA = 1;


   //Staff members table
    public static final String TABLE_STAFF_MEMBERS = "staffMembers";

    public static final String TABLE_STAFF_MEMBERS_ID = "_id";
    public static final String TABLE_STAFF_MEMBERS_NAME= "name";
    public static final String TABLE_STAFF_MEMBERS_PASSWORD = "password";
    public static final String TABLE_STAFF_MEMBERS_STATUS = "status";

    //Products table
    public static final String TABLE_PRODUCTS = "products";

    public static final String TABLE_PRODUCT_ID = "_id";
    public static final String TABLE_PRODUCT_NAME= "name";
    public static final String TABLE_PRODUCT_PRICE = "price";
    public static final String TABLE_PRODUCT_CATEGORY = "category";


    public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, SCHEMA);
	}

	public static DatabaseHelper getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(ctx.getApplicationContext());
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
			db.execSQL("CREATE TABLE " + TABLE_STAFF_MEMBERS + " (" + TABLE_STAFF_MEMBERS_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_STAFF_MEMBERS_NAME
					+ " TEXT," + TABLE_STAFF_MEMBERS_PASSWORD + " TEXT," + TABLE_STAFF_MEMBERS_STATUS
					+ " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" + TABLE_PRODUCT_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_PRODUCT_NAME
                    + " TEXT," + TABLE_PRODUCT_PRICE + " REAL," + TABLE_PRODUCT_CATEGORY
                    + " TEXT);");

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
