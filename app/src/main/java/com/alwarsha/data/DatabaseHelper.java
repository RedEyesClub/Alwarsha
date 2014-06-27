package com.alwarsha.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper mInstance = null;

	private static final String DB_NAME = "alwarsha.db";
    private static final String DB_PATH = "/data/data/com.alwarsha.app/databases/";
	private static final int SCHEMA = 1;
    private Context mContext;


   //Staff members table
    public static final String TABLE_STAFF_MEMBERS = "staffMembers";

    public static final String TABLE_STAFF_MEMBERS_ID = "member_id";
    public static final String TABLE_STAFF_MEMBERS_NAME= "name";
    public static final String TABLE_STAFF_MEMBERS_PASSWORD = "password";
    public static final String TABLE_STAFF_MEMBERS_STATUS = "status";

    //Products table
    public static final String TABLE_PRODUCTS = "products";

    public static final String TABLE_PRODUCT_ID = "product_id";
    public static final String TABLE_PRODUCT_NAME = "name";
    public static final String TABLE_PRODUCT_NAME_AR = "name_ar";
    public static final String TABLE_PRODUCT_PRICE = "price";
    public static final String TABLE_PRODUCT_CAT_ID = "category_id";
    public static final String TABLE_PRODUCT_PIC_NAME = "picture_name";

    //Products Groups table
    public static final String TABLE_PRODUCTS_GROUPS = "products_groups";

    public static final String TABLE_PRODUCT_GROUP_ID = "group_id";
    public static final String TABLE_PRODUCT_GROUP_NAME = "name";
    public static final String TABLE_PRODUCT_GROUP_NAME_AR = "name_ar";
    public static final String TABLE_PRODUCT_GROUP_PIC_NAME = "picture_name";

    //Products Categories table
    public static final String TABLE_PRODUCTS_CATEGORIES = "products_categories";

    public static final String TABLE_PRODUCT_CATEGORY_ID = "category_id";
    public static final String TABLE_PRODUCT_CATEGORY_GROUP_ID = "cat_group_id";
    public static final String TABLE_PRODUCT_CATEGORY_NAME = "name";
    public static final String TABLE_PRODUCT_CATEGORY_NAME_AR = "name_ar";
    public static final String TABLE_PRODUCT_CATEGORY_PIC_NAME = "picture_name";

    //Deals table
    public static final String TABLE_DEALS = "deals";

    public static final String TABLE_DEAL_ID = "_id";
    public static final String TABLE_DEAL_NAME = "deal_name";
    public static final String TABLE_DEAL_OPEN_TIME = "open_time";
    public static final String TABLE_DEAL_CLOSE_TIME = "close_time";
    public static final String TABLE_DEAL_STATUS = "status";
    public static final String TABLE_DEAL_TOTAL = "total";
    public static final String TABLE_DEAL_DISCOUNT = "discount";
    public static final String TABLE_DEAL_COMMENT = "comment";

    //Deals product table
    public static final String TABLE_DEALS_PRODUCT = "deals_product";

    public static final String TABLE_DEALS_PRODUCT_ID = "_id";
    public static final String TABLE_DEALS_PRODUCT_PRODUCT_ID = "deals_product_product_id";
    public static final String TABLE_DEALS_PRODUCT_STATUS = "deals_product_status";
    public static final String TABLE_DEALS_PRODUCT_DEAL_ID = "deals_product_deal_id";






    public DatabaseHelper(Context context) {

		super(context, DB_NAME, null, SCHEMA);
        mContext = context;

        boolean dbexist = checkdatabase();
        if (dbexist) {
            //System.out.println("Database exists");
            try {
                opendatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Database doesn't exist");
            try {
                createdatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
//			db.execSQL("CREATE TABLE " + TABLE_STAFF_MEMBERS
//                    + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + TABLE_STAFF_MEMBERS_ID + " INTEGER,"
//                    + TABLE_STAFF_MEMBERS_NAME + " TEXT,"
//                    + TABLE_STAFF_MEMBERS_PASSWORD + " TEXT,"
//                    + TABLE_STAFF_MEMBERS_STATUS + " TEXT);");
//            db.execSQL("CREATE TABLE " + TABLE_PRODUCTS
//                    + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + TABLE_PRODUCT_ID + " INTEGER, "
//                    + TABLE_PRODUCT_NAME + " TEXT, "
//                    + TABLE_PRODUCT_NAME_AR + " TEXT, "
//                    + TABLE_PRODUCT_PRICE + " REAL, "
//                    + TABLE_PRODUCT_CAT_ID + " INTEGER, "
//                    + TABLE_PRODUCT_PIC_NAME + " TEXT);");
//            db.execSQL("CREATE TABLE " + TABLE_PRODUCTS_GROUPS
//                    + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + TABLE_PRODUCT_GROUP_ID + " INTEGER,"
//                    + TABLE_PRODUCT_GROUP_NAME + " TEXT,"
//                    + TABLE_PRODUCT_GROUP_NAME_AR + " TEXT, "
//                    + TABLE_PRODUCT_GROUP_PIC_NAME + " TEXT);");
//            db.execSQL("CREATE TABLE " + TABLE_PRODUCTS_CATEGORIES
//                    + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + TABLE_PRODUCT_CATEGORY_ID + " INTEGER, "
//                    + TABLE_PRODUCT_CATEGORY_GROUP_ID + " INTEGER, "
//                    + TABLE_PRODUCT_CATEGORY_NAME + " TEXT, "
//                    + TABLE_PRODUCT_CATEGORY_NAME_AR + " TEXT,"
//                    + TABLE_PRODUCT_CATEGORY_PIC_NAME + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_DEALS
                    + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_DEAL_NAME + " TEXT, "
                    + TABLE_DEAL_OPEN_TIME + " TEXT, "
                    + TABLE_DEAL_CLOSE_TIME + " TEXT, "
                    + TABLE_DEAL_STATUS + " TEXT,"
                    + TABLE_DEAL_TOTAL + " REAL,"
                    + TABLE_DEAL_DISCOUNT + " REAL,"
                    + TABLE_DEAL_COMMENT + " TEXT);");
            db.execSQL("CREATE TABLE " + TABLE_DEALS_PRODUCT
                    + " (" + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_DEALS_PRODUCT_PRODUCT_ID + " INTEGER, "
                    + TABLE_DEALS_PRODUCT_STATUS + " TEXT, "
                    + TABLE_DEALS_PRODUCT_DEAL_ID + " INTEGER);");

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if(dbexist) {
            //System.out.println(" Database exists.");
        } else {
            SQLiteDatabase s = this.getReadableDatabase();
            if(s == null)
                Log.i("s adataBase == null","error");
            try {
                copydatabase();
            } catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkdatabase() {
        SQLiteDatabase checkdb = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        if(checkdb != null)
        {
            checkdb.close();
        }
        return (checkdb != null);
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        if (mContext == null){
            Log.i("context", "null");
        }
        if (mContext.getAssets() == null){
            Log.i("assets", "null");
        }
        InputStream myinput = mContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_PATH + DB_NAME;
        SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

}
