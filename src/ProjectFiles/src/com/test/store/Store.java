package com.test.store;

import com.test.database.Database;
import com.test.user.User;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Store {
    private Integer storeID;
    private User storeOwner;
    private final LocalDate createdDate;
    private List<String> products;

    private static Database database = new Database();
    private static Integer ID = 0;

    public Store(User storeOwner, LocalDate createdDate) {
        this.storeID = ++ID;
        this.storeOwner = storeOwner;
        this.createdDate = createdDate;
        this.products = new ArrayList<String>();
    }

    public void saveStoreToDB(){
        database.connectToDB();
        database.saveData("STORES", "STORES");
        System.out.println("Store has been saved to DB");
        database.closeConnection();
    }

    public User getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(User storeOwner) {
        this.storeOwner = storeOwner;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public List<String> getProducts() {
        return products;
    }

    public void addProduct(List<String> products) {
        List<String> currentProducts = new ArrayList<>(this.products.size() + products.size());
        currentProducts.addAll(this.products);
        currentProducts.addAll(products);
        this.products = currentProducts;
    }
}
