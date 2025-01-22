package com.example.urs_2024_25;

public class UserModel {
    private String name;
    private String surname;
    private Long userID;

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }
}
