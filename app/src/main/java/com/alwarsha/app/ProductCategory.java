package com.alwarsha.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farid on 4/20/14.
 */
public class ProductCategory {

    private int mId;
    private String mName;
    private String mPicName;
    private List<Product> mProductsList = new ArrayList<Product>();

    public ProductCategory(int mId, String mName, String mPicName, List<Product> mProductsList) {
        this.mId = mId;
        this.mName = mName;
        this.mPicName = mPicName;
        this.mProductsList = mProductsList;
    }

    public List<Product> getmProductsList() {
        return mProductsList;
    }

    public void setmProductsList(List<Product> mProductsList) {
        this.mProductsList = mProductsList;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPicName() {
        return mPicName;
    }

    public void setmPicName(String mPicName) {
        this.mPicName = mPicName;
    }



}
