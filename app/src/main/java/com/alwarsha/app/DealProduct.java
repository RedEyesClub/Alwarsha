package com.alwarsha.app;

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

    public int getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(int deal_id) {
        this.deal_id = deal_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DealProduct(int id, String name,int categoryId, String pictureName, float price,String language) {
      super(id,name,categoryId,pictureName,price,language);
        mStatus = DealProductStatus.ORDERED;
    }

    public DealProduct(Product p,DealProductStatus status) {
        super(p);
        mStatus = status;
        this.id = id;
        this.deal_id = deal_id;
    }

    public DealProductStatus getStatus() {
        return mStatus;
    }

    public void setStatus(DealProductStatus mStatus) {
        this.mStatus = mStatus;
    }

//    public void setStatus(int mStatus) {
//        this.mStatus.value = mStatus;
//    }
}
