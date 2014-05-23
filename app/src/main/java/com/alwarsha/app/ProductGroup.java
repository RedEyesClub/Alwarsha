package com.alwarsha.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samerm on 5/2/14.
 */
public class ProductGroup {

    private int mId;
    private HashMap<String,String> mName;
    private String mPicName;

    public ProductGroup(int mId, String mName, String mPicName, String lang) {
        this.mId = mId;
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
