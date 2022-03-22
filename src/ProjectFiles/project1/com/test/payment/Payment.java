package com.test.payment;

import com.test.credit_card.Card;
import com.test.database.Database;
import com.test.store.Item;
import com.test.user.User;

import java.util.List;

public class Payment{
    public static boolean processPayment(User user, Card card, List<Item> items){
        System.out.println("Processing payment...");
        Database.query("INSERT INTO ...");
        return true;
    }
}
