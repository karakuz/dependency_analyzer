package com.test.account;

import com.test.database.Database;
import com.test.user.User;

import java.time.LocalDate;

public class Account {
    private User user;
    private LocalDate createdDate;
    private double balance = 0;
    private String userType;

    private static Database database = new Database();

    public Account(User user, LocalDate createdDate, String userType) {
        this.user = user;
        this.createdDate = createdDate;
        this.userType = userType;
    }

    public void saveAccountToDB(){
        database.connectToDB();

        System.out.println("Saving: " + this.user.toString());
        boolean success = database.saveData("DB_NAME", "Customers");
        if(success)
            System.out.println("Account has been saved");
        database.closeConnection();
    }

    public User getUser() {
        return user;
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
            database.connectToDB();
            database.saveData("CUSTOMERS", "CUSTOMERS");
            database.closeConnection();
        }
        else
            System.out.println("User is not Type Customer");
    }
}
