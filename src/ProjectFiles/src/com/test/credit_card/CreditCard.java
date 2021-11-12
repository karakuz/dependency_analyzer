package com.test.credit_card;

import com.test.database.Database;
import com.test.user.User;

public interface CreditCard {
    public static final Database database = new Database();

    public void addToUser(User user);
    public void deleteFromUser(User user);
    public void getCardCredentials();
    public String getCardNumber();
}
