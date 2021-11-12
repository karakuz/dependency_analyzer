package com.test.credit_card;

import com.test.user.User;

public class Visa extends Card{
    private String cardNumber;

    public Visa(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}