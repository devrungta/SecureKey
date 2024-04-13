package com.example.securekey;

public class App {
    private long userID;
    private String name;
    private String password;

    public App(String name, String password, long userID) {
        this.name = name;
        this.password = password;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
