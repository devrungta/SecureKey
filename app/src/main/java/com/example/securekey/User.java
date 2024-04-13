package com.example.securekey;

public class User {
    private long userId;
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private String gender;

    public User(long userId, String username, String phoneNumber, String email, String password, String gender) {
        this.userId = userId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }



    // Getter methods
    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }
}
