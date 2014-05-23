package com.alwarsha.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Farid on 4/20/14.
 */
public class ProductCategory {

    private int mId;
    private int mGroupId;
    private HashMap<String,String> mName;
    private String mPicName;

    public ProductCategory(int mId, int mGroupId, String mName, String mPicName, String lang) {
        this.mId = mId;
        this.mGroupId = mGroupId;
        this.mName = new HashMap<String, String>();
        this.mName.put(lang,mName);
        this.mPicName = mPicName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmGroupId() {
        return mGroupId;
    }

    public void setmGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    public String getmName(String lang) {
        return mName.get(lang);
    }

    public void setmName(String mName,String lang) {
        this.mName.put(lang,mName);
    }

    public String getmPicName() {
        return mPicName;
    }

    public void setmPicName(String mPicName) {
        this.mPicName = mPicName;
    }

    public void addName(String lang, String name) {
        this.mName.put(lang, name);
    }
}
