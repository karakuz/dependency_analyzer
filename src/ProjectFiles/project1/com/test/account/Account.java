package com.test.account;

import com.test.credit_card.Card;
import com.test.database.Database;
import com.test.user.User;

import java.time.LocalDate;

public class Account {
    private LocalDate createdDate;
    private double balance = 0;
    private String userType;
    protected Card card = null;


    private static Database database = new Database();

    public Account(LocalDate createdDate, String userType) {
        this.createdDate = createdDate;
        this.userType = userType;
    }

    public LocalDate getCreateDate() {
        return createdDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if(userType.equals("CUSTOMER")){
            this.balance = balance;
            Database.connectToDB();
            Database.saveData("CUSTOMERS", "CUSTOMERS");
            Database.closeConnection();
        }
        else
            System.out.println("User is not Type Customer");
    }

    public String getUserType() {
        return userType;
    }
}
