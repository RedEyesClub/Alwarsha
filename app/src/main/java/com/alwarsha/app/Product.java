package com.alwarsha.app;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.HashMap;

/**
 * Created by Farid on 4/19/14.
 */
public class Product {
    private int mId;
    private HashMap<String, String> mName;
    private int mCategoryId;
    private String mPictureName;
    private float mPrice;

    public Product(Product p){
        this.mId = p.getmId();
        if(p.getmName("EN") != null)
            this.mName.put("EN", p.getmName("EN"));
        if(p.getmName("AR") != null)
            this.mName.put("AR", p.getmName("AR"));
        this.mCategoryId = p.getmCategoryId();
        this.mPictureName = p.getmPictureName();
        this.mPrice = p.getmPrice();
    }

    public Product(int mId, String mName, int mCategoryId, String mPictureName, float mPrice, String language) {
        this.mId = mId;
        this.mName = new HashMap<String, String>();
        this.mName.put(language,mName);
        this.mCategoryId = mCategoryId;
        this.mPictureName = mPictureName;
        this.mPrice = mPrice;
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

    public void setmName(String mName, String lang) {
        this.mName.put(lang,mName);
    }

    public String getmPictureName() {
        return mPictureName;
    }

    public void setmPictureName(String mPictureName) {
        this.mPictureName = mPictureName;
    }

    public float getmPrice() {
        return mPrice;
    }

    public void setmPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    public int getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public void addName(String lang, String name) {
        this.mName.put(lang, name);
    }
}
