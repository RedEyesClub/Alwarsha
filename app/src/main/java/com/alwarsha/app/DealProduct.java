package com.alwarsha.app;

/**
 * Created by Farid on 5/5/14.
 */
public class DealProduct extends Product {

    public enum  DealProductStatus {ORDERED,SENT,DONE}

    private DealProductStatus mStatus;

    public DealProduct(int id, String name,int categoryId, String pictureName, float price,String language) {
      super(id,name,categoryId,pictureName,price,language);
    }

    public DealProduct(Product p,DealProductStatus status) {
        super(p);
        mStatus = status;
    }

    public DealProductStatus getStatus() {
        return mStatus;
    }

    public void setStatus(DealProductStatus mStatus) {
        this.mStatus = mStatus;
    }
}
