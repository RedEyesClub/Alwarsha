package com.alwarsha.app;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;

import com.alwarsha.app.StaffMember;
import com.alwarsha.data.StaffMembersProvider;

/**
 * Created by Farid on 4/19/14.
 */
public class AlwarshaApp extends Application {
    public static boolean DEBUG = true;
    private static AlwarshaApp ourInstance = new AlwarshaApp();
    public static  StaffMember m;

    public static AlwarshaApp getInstance() {
        return ourInstance;
    }

    private AlwarshaApp() {

//        List<Product> beersList = new ArrayList<Product>();
//DB integration
//        beersList.add(new Product(0, "Calsberg 1/3", "calsberg1_3", 20));
//        beersList.add(new Product(1, "Tuborg 1/3", "tuborg_g_1_3", 20));
//        beersList.add(new Product(2, "Tuborg 1/3", "tuborg_r_1_3", 20));
//        beersList.add(new Product(4, "Heineken 1/3", "heineken", 20));
//        beersList.add(new Product(5, "BECK'S 1/3s","becks", 20));
//        beersList.add(new Product(3, "Stella", "stella", 20));

//DB integration
//        ProductCategory beers = new ProductCategory(0,"Beers","beers",beersList);
//        ProductCategory hotDrinks = new ProductCategory(1,"Hot drinks","hot_drinks",null);
//       ProductCategory alcohol = new ProductCategory(2,"Alcohol","alcohol",null);
//        ProductCategory softdrinks = new ProductCategory(3,"Soft Drinks","soft_drinks",null);



//DB integration
//        List<ProductCategory> productsCategoryList = new ArrayList<ProductCategory>();
//        productsCategoryList.add(beers);
//        productsCategoryList.add(hotDrinks);
//        productsCategoryList.add(alcohol);
//        productsCategoryList.add(softdrinks);


//DB integration
  //      mMenue = new Menu(productsCategoryList);
    }

}
