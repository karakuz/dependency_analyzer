package com.test;

import com.test.account.Account;
import com.test.account.UserType;
import com.test.store.Store;
import com.test.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
	    User admin = new User("Emre Karakuz", 23);
        Account account = admin.createAccount("ADMIN");
        account.saveAccountToDB();

        User storeOwner = new User("Store Owner", 30);
        Account storeOwnerAccount = storeOwner.createAccount("STORE_OWNER");
        Store store = storeOwner.createStore();
        storeOwnerAccount.saveAccountToDB();

        List<String> products = new ArrayList<>(Arrays.asList("Elma", "Armut"));
        store.addProduct(products);

        System.out.println("Products of StoreID " + store.getStoreID() + ": ");
        for (String product : store.getProducts())
            System.out.println(product);


        User customer = new User("TestCustomer", 35);
        Account customerAccount = customer.createAccount("CUSTOMER");
        customerAccount.saveAccountToDB();
        customerAccount.setBalance(1000);

        System.out.println("Balance of " + customer.getName() + " : " + customerAccount.getBalance());
    }
}
