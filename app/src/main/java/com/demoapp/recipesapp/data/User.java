package com.demoapp.recipesapp.data;

public class User {
    private String name;
    private String lastname;
    private int age;
    private String email;
    private String password;
    private String tokenUID;

    public User(String email, String tokenUID) {
        this.email = email;
        this.tokenUID = tokenUID;
    }

    public User(String email, String password, String tokenUID) {
        this(email, tokenUID);
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getTokenUID() {
        return tokenUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
