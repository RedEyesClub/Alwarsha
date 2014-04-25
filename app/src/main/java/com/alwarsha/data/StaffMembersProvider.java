package com.alwarsha.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.StaffMember;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class StaffMembersProvider {

    private static String TAG = "StaffMembersProvider";

	private String[] mAllColumns = {
			DatabaseHelper.TABLE_STAFF_MEMBERS_ID, DatabaseHelper.TABLE_STAFF_MEMBERS_NAME,
			DatabaseHelper.TABLE_STAFF_MEMBERS_PASSWORD, DatabaseHelper.TABLE_STAFF_MEMBERS_STATUS};

	private DatabaseHelper mDbHelper;
	private Context mContext;

    @Root (name="StaffMembers")
    public static class StaffMembers{
        @ElementList (name="members")
        public ArrayList<StaffMember> members;

        public StaffMembers()
        {
            members = new ArrayList<StaffMember>();
        }
    }

	// Singleton
	private static StaffMembersProvider sInstace = null;

	public static StaffMembersProvider getInstace(Context context) {
		if (sInstace == null) {
			sInstace = new StaffMembersProvider(context);
		}
		return sInstace;
	}

	private StaffMembersProvider(Context context) {
		super();
		mContext = context;
		mDbHelper = DatabaseHelper.getInstance(mContext);
	}

	public void close() {
		mDbHelper.close();
	}
	


	public void insertNewStaffMember(StaffMember member) {
//		SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
//        if(member.getId())
//		Cursor cursor = db1.query(DatabaseHelper.TABLE_STAFF_MEMBERS, mAllColumns,
//				DatabaseHelper.TABLE_STAFF_MEMBERS_ID + "= ?"  , new String[]{member.getId()}, null, null, null);
//		if(cursor != null && cursor.getCount() != 0 ){
//			return;
//		}
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		//cv.put(DatabaseHelper.TABLE_STAFF_MEMBERS_ID, member.getId());
		cv.put(DatabaseHelper.TABLE_STAFF_MEMBERS_NAME, member.getName());
		cv.put(DatabaseHelper.TABLE_STAFF_MEMBERS_PASSWORD, member.getPassword());
		cv.put(DatabaseHelper.TABLE_STAFF_MEMBERS_STATUS, member.getStatus());

		long i  = db.insert(DatabaseHelper.TABLE_STAFF_MEMBERS, null, cv);
        Log.d(TAG, "insert return value = " + i);
	}

	public StaffMember getStaffMember(String username,String password) {

        StaffMember member = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
		 db = mDbHelper.getReadableDatabase();
         cursor = db.query(DatabaseHelper.TABLE_STAFF_MEMBERS, mAllColumns,
				DatabaseHelper.TABLE_STAFF_MEMBERS_NAME + "=? AND " + DatabaseHelper.TABLE_STAFF_MEMBERS_PASSWORD + "=?"
                ,new String[]{username,password}, null, null, null, null);
        }catch (NullPointerException e){
            if(AlwarshaApp.DEBUG)
                Log.e(TAG,"Exception at getStaffMember" );
        }
		if (cursor.moveToFirst()) {
			String status = cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.TABLE_STAFF_MEMBERS_STATUS));
			cursor.close();

			member = new StaffMember(status,username, password);
		}
		return member;
	}

    public void StaffMemeber_deleteDB()
    {
        SQLiteDatabase db = null;
        try{
            db = mDbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_STAFF_MEMBERS, null, null);

        }catch (NullPointerException e){
            if(AlwarshaApp.DEBUG)
                Log.e(TAG,"Exception at delete StaffMembers Table" );
        }
    }

    public boolean StaffMemeber_initDataBase(File xml_file)
    {
        if (xml_file.exists() == false) {
            return false;
        }

        Serializer serializer = new Persister();
        StaffMembers staff_members = new StaffMembers();
        try {
            staff_members = serializer.read(StaffMembers.class, xml_file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(staff_members == null)
        {
            return false;
        }

        StaffMemeber_deleteDB();

        for(StaffMember sm : staff_members.members)
        {
            insertNewStaffMember(sm);
        }
        return true;
    }
}
