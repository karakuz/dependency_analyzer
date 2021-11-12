package com.test.user;

import com.test.account.Account;
import com.test.account.UserType;
import com.test.credit_card.Card;
import com.test.database.Database;
import com.test.store.Store;

import java.time.LocalDate;

public class User extends Account{
    private String name;
    private int age;

    public User(String name, int age, String userType) {
        super(LocalDate.now(), userType);
        this.name = name;
        this.age = age;
    }

    public Store createStore(){
        Store newStore = new Store(this, LocalDate.now());
        newStore.saveStoreToDB();

        return newStore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void saveAccountToDB(){
        Database.connectToDB();

        System.out.println("Saving: " + this.toString());
        boolean success = Database.saveData("DB_NAME", "Customers");
        if(success)
            System.out.println("Account has been saved");
        Database.closeConnection();
    }

    public User getUser() {
        return this;
    }

    public boolean addCardToUser(Card card){
        if(!this.getUserType().equals("CUSTOMER")) return false;

        this.card = card;
        card.addToUser(this);
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }


}
