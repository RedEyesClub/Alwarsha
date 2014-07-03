package com.alwarsha.app;

import android.content.Context;

import com.alwarsha.data.DealsProductProvider;

/**
 * Created by Farid on 5/5/14.
 */
public class DealProduct extends Product {

    public enum  DealProductStatus {
        ORDERED("ORDERED"),SENT("SENT"),DONE("DONE");

        private String status;
        private DealProductStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString(){
            return status;
        }

        public static DealProductStatus fromString(String x) {
            if(x.equals("ORDERED"))
                    return ORDERED;
            if(x.equals("SENT"))
                    return SENT;
            if(x.equals("DONE"))
                    return DONE;

            return null;
        }
    }

    private DealProductStatus mStatus;
    private int id;
    private int deal_id;
    private String mComment;

    public int getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(int deal_id,Context context) {
        this.deal_id = deal_id;
        saveInDB(context);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment,Context context) {
        this.mComment = comment;
        saveInDB(context);
    }


    public DealProduct(int id, String name,int categoryId, String pictureName, float price,String language) {
      super(id,name,categoryId,pictureName,price,language);
        mStatus = DealProductStatus.ORDERED;
    }

    public DealProduct(Product p,DealProductStatus status,Context context) {
        super(p);
        this.mStatus = status;
        saveInDB(context);
    }

    public DealProductStatus getStatus() {
        return mStatus;
    }

    public void setStatus(DealProductStatus mStatus, Context context) {
        this.mStatus = mStatus;
        saveInDB(context);
    }


    private void saveInDB(Context context){
        DealsProductProvider dpp = DealsProductProvider.getInstace(context);
        dpp.updateDealProduct(this);
    }
}
