package com.alwarsha.app;

/**
 * Created by Farid on 5/5/14.
 */
public class DealProduct extends Product {

    public enum  DealProductStatus {ORDERED,SENT,DONE}

    private DealProductStatus mStatus;

    public DealProduct(int id, String name, String pictureName, float price) {
      super(id,name,pictureName,price);
    }

    public DealProduct(Product p,DealProductStatus status) {
        super(p.getmId(),p.getName(),p.getmPictureName(),p.getmPrice());
        mStatus = status;
    }

    public DealProductStatus getStatus() {
        return mStatus;
    }

    public void setStatus(DealProductStatus mStatus) {
        this.mStatus = mStatus;
    }
}
