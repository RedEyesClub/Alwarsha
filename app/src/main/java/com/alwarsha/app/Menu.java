package com.alwarsha.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farid on 4/20/14.
 */
public class Menu {
    private List<ProductCategory> mProductsCategory = new ArrayList<ProductCategory>();

    public Menu(List<ProductCategory> mProductsCategory) {
        this.mProductsCategory = mProductsCategory;
    }

    public List<ProductCategory> getmProductsCategory() {
        return mProductsCategory;
    }

    public void setmProductsCategory(List<ProductCategory> mProductsCategory) {
        this.mProductsCategory = mProductsCategory;
    }
}
