package com.alwarsha.app;

import java.util.Date;
import java.util.List;

/**
 * Created by Halani on 4/24/14.
 */
public class Deal {

    public static enum DEAL_STATUS{
        OPEN,
        CLOSED
    }

    private int id;
    private String name;
    private List<Product> products;
    private DEAL_STATUS status;
    private float total;
    private float total_discount;
    private Date open;
    private Date close;

}
