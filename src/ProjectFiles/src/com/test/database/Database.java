package com.test.database;

public class Database {
    private static final String CONNECTION_STRING = "";
    private static final String DB_PASSWORD = "";
    private static final String PORT = "";

    public void connectToDB(){
        System.out.println("Connected to DB");
    }

    public void closeConnection(){
        System.out.println("DB Connection has been closed");
    }

    public void getData(String query){
        System.out.println("Getting data from DB...");
    }

    public boolean saveData(String databaseName, String tableName){
        System.out.println("Data has been saved to DB");
        return true;
    }

}
