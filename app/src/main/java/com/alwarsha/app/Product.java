package com.alwarsha.app;

/**
 * Created by Farid on 4/19/14.
 */
public class Product {
    private int mId;
    private String mName;
    private String mPictureName;
    private float mPrice;

    public Product(int mId, String mName, String mPictureName, float mPrice) {
        this.mId = mId;
        this.mName = mName;
        this.mPictureName = mPictureName;
        this.mPrice = mPrice;
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


}
