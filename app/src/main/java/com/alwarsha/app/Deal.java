package com.alwarsha.app;

import android.content.Context;
import android.util.Log;

import com.alwarsha.data.DealsProductProvider;
import com.alwarsha.data.DealsProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Halani on 4/24/14.
 */
public class Deal {
    DealsProvider mDealProvider;

    private String mOrdersToSend = "";

    private String mDealConclosion = "";

    public static enum DEAL_STATUS {
        OPEN("OPEN"),
        CLOSED("CLOSED");

        private String status;
        private DEAL_STATUS(String status) {
            this.status = status;
        }

        @Override
        public String toString(){
            return status;
        }

        public static DEAL_STATUS fromString(String x) {
            if(x.equals("OPEN"))
                return OPEN;
            if(x.equals("CLOSED"))
                return CLOSED;

            return null;
        }
    }

    public Deal() { }

    public Deal(String name, Context context) throws Exception {
        this.name = name;
        this.open = new Date();
        this.status = DEAL_STATUS.OPEN;
        this.total = 0;
        this.total_discount = 0;
        this.comment = "";
        this.close = new Date();
        this.id = -1;

        mDealProvider = DealsProvider.getInstace(context);
        try{
            this.id = mDealProvider.insertNewDeal(this);
        }
        catch(Exception ex){
            throw (ex);
        }

        this.myContext = context;

    }

    public Deal(Deal d) {

       // Deal s = mDealProvider.getOpenDealByName(d.getName());

        this.name = d.getName();
        this.status = d.getStatus();
        this.total = d.getTotal();
        this.mProducts = d.getmProducts();
        this.total_discount = d.getTotal_discount();
        this.open = d.getOpen();
        if (d.getClose() != null)
            this.close = d.getClose();
        this.id = d.getId();
        this.comment = "";
        this.myContext = d.myContext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String name;

    private List<DealProduct> mProducts = new ArrayList<DealProduct>();
    private DEAL_STATUS status;
    private float total;
    private float total_discount;
    private Date open;
    private Date close;
    private String comment;
    private Context myContext;

    public Context getMyContext() {
        return myContext;
    }

    public void setMyContext(Context myContext) {
        this.myContext = myContext;
    }

    private void saveInDB(Context context){
        DealsProvider dp = DealsProvider.getInstace(context);
        dp.updateDeal(this);
    }

    public boolean addProduct(DealProduct product, Context context) {

        DealsProductProvider dpp = DealsProductProvider.getInstace(context);
        product.setDeal_id(this.id,context);
        product.setId(dpp.insertNewDealProduct(product));
        if(mProducts == null){
            mProducts = new ArrayList<DealProduct>();
        }
        mProducts.add(product);

        this.total += product.getmPrice();

        saveInDB(context);

        return true;
    }

    public DealProduct.DealProductStatus delete_product(int product_id, Context context){
        DealProduct dealProduct = null;
        for(DealProduct dp: mProducts ){
            if((dp.getmId() == product_id) && (dp.getStatus() == DealProduct.DealProductStatus.ORDERED)){
                dealProduct = dp;
                break;
            }
        }
        if(dealProduct == null){
            for(DealProduct dp: mProducts ){
                if((dp.getmId() == product_id) && (dp.getStatus() == DealProduct.DealProductStatus.SENT)){
                    dealProduct = dp;
                    break;
                }
            }
        }
        if(dealProduct == null){
            return null;
        }
        DealsProductProvider dpp = DealsProductProvider.getInstace(context);
        dpp.deleteDealProduct(dealProduct.getId());
        DealProduct.DealProductStatus ret_val = dealProduct.getStatus();

        dpp.deleteDealProduct(dealProduct.getId());
        this.total -= dealProduct.getmPrice();

        mProducts.remove(dealProduct);
        saveInDB(context);
        return ret_val;
    }

    public void setDealComment(String new_comment, Context context){
        this.comment = new_comment;
        saveInDB(context);
    }

    public void setTotalDiscount(float new_discount, Context context){
        this.total_discount = new_discount;
        saveInDB(context);
    }

    public String getOrdersToSend() {
        return mOrdersToSend;
    }

    public void setOrdersToSend(String mOrdersToSend) {
        this.mOrdersToSend = mOrdersToSend;
    }

    public String getDealConclosion() {
        return mDealConclosion;
    }

    public void setDealConclosion(String mDealConclosion) {
        this.mDealConclosion = mDealConclosion;
    }

    public void close(Context context){
        if(status == DEAL_STATUS.CLOSED){
            return;
        }
        this.status = DEAL_STATUS.CLOSED;
        this.close = new Date();
        saveInDB(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DealProduct> getmProducts() {
        return mProducts;
    }

    public void setmProducts(List<DealProduct> mProducts) {
        this.mProducts = mProducts;
    }

    public DEAL_STATUS getStatus() {
        return status;
    }

    public void setStatus(DEAL_STATUS status) {
        this.status = status;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(float total_discount) {
        this.total_discount = total_discount;
    }

    public Date getOpen() {
        return open;
    }

    public void setOpen(Date open) {
        this.open = open;
    }

    public Date getClose() {
        return close;
    }

    public void setClose(Date close) {
        this.close = close;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
