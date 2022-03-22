package com.test.credit_card;

import com.test.user.User;

public class Card implements CreditCard{
    public void addToUser(User user) {
        database.query("test query in createCard");
    }

    public void deleteFromUser(User user) {
        database.query("test query in deleteCard");
    }

    public void getCardCredentials() {
        database.query("test query in getCardCredential");
    }

    public String getCardNumber() {
        database.query("test query in getCardNumber");
        return "";
    }
}
