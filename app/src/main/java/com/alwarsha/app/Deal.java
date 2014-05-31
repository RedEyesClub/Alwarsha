package com.alwarsha.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Halani on 4/24/14.
 */
public class Deal {

    public static enum DEAL_STATUS {
        OPEN,
        CLOSED
    }

    public Deal(String name, Date open) {
        this.name = name;
        this.open = open;
    }

    public Deal(Deal d) {
        this.id = d.getId();
        this.name = d.getName();
        this.status = d.getStatus();
        this.total = d.getTotal();
        this.mProducts = d.getmProducts();
        this.total_discount = d.getTotal_discount();
        this.open = d.getOpen();
        if (d.getClose() != null)
            this.close = d.getClose();
    }

    private int id;
    private String name;
    private List<DealProduct> mProducts = new ArrayList<DealProduct>();
    private DEAL_STATUS status;
    private float total;
    private float total_discount;
    private Date open;
    private Date close;

    public int getId() {
        return id;
    }

    public boolean addProduct(DealProduct product) {

        mProducts.add(product);

//        //DB integration
//        String productName = "temp";//product.getName();
//        Iterator it = mProducts.entrySet().iterator();
////        if (!it.hasNext()) {
////            mProducts.put(product, 1);
////           // it.remove();
////        } else {
//        boolean founded = false;
//        while (it.hasNext()) {
//            Map.Entry pairs = (Map.Entry) it.next();
//            Integer r = 0;
//            if (pairs != null)
//                r = (Integer) pairs.getValue();
//            //DB integration
//            //if (((DealProduct) (pairs.getKey())).getName().equals(productName)) {
//            //    mProducts.put((DealProduct) (pairs.getKey()), r + 1);
//            //    founded = true;
//            //}
//        }
//        if (!founded) {
//            mProducts.put(product, 1);
//        }

        //      }

//
//        if(r!= null){
//            mProducts.put(product, r + 1);
//        }else{
//            mProducts.put(product,1);
//        }
        return true;
    }

    public void setId(int id) {
        this.id = id;
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


}
