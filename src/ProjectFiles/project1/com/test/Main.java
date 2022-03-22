package com.test;

import com.test.account.Account;
import com.test.credit_card.MasterCard;
import com.test.credit_card.Visa;
import com.test.payment.Payment;
import com.test.store.Item;
import com.test.store.Store;
import com.test.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
	    User admin = new User("Emre Karakuz", 23, "ADMIN");
        admin.saveAccountToDB();

        User storeOwner = new User("Store Owner", 30, "STORE_OWNER");
        Store store = storeOwner.createStore();
        storeOwner.saveAccountToDB();

        Item elma = new Item("Elma", "Yiyecek", store, 10.5, 100);
        Item armut = new Item("Armut", "Yiyecek", store, 9.0, 150);

        List<Item> products = new ArrayList<>(Arrays.asList(elma,armut));
        store.addProduct(products);

        User customer = new User("TestCustomer", 35, "CUSTOMER");
        customer.saveAccountToDB();
        customer.setBalance(1000);

        System.out.println("Balance of " + customer.getName() + " : " + customer.getBalance());

        MasterCard newMasterCard = new MasterCard( "1234-5678-9012-3456");
        newMasterCard.addToUser(customer);

        Visa newVisaCard = new Visa( "1234-5678-9012-3456");
        customer.addCardToUser(newVisaCard);

        Payment.processPayment(customer, newMasterCard, Arrays.asList(elma));
    }
}
