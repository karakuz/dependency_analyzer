package com.test.user;

import com.test.account.Account;
import com.test.account.UserType;
import com.test.store.Store;

import java.time.LocalDate;

public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Account createAccount(String userType){
        Account newAccount = new Account(this, LocalDate.now(), userType);
        return newAccount;
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
