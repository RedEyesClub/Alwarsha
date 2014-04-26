package com.alwarsha.app;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Halani on 4/24/14.
 */
public class Deal {

    public static enum DEAL_STATUS{
        OPEN,
        CLOSED
    }

    public Deal(String name, Date open) {
        this.name = name;
        this.open = open;
    }

    public Deal(Deal d){
        this.id = d.getId();
        this.name = d.getName();
        this.status = d.getStatus();
        this.total = d.getTotal();
        this.mProducts = d.getmProducts();
        this.total_discount  = d.getTotal_discount();
        this.open = d.getOpen();
        if(d.getClose() != null)
            this.close = d.getClose();
    }

    private int id;
    private String name;
    private HashMap<Product,Integer> mProducts = new HashMap<Product, Integer>();
    private DEAL_STATUS status;
    private float total;
    private float total_discount;
    private Date open;
    private Date close;

    public int getId() {
        return id;
    }

    public boolean addProduct(Product product){
        Integer r = mProducts.get(product);
        if(r!= null){
            mProducts.put(product, mProducts.get(product) + 1);
        }else{
            mProducts.put(product,1);
        }
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

    public HashMap<Product, Integer> getmProducts() {
        return mProducts;
    }

    public void setmProducts(HashMap<Product, Integer> mProducts) {
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
