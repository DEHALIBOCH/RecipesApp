package com.demoapp.recipesapp.data;

import java.util.ArrayList;

public class User {
    private String name;
    private String lastname;
    private int age;
    private String email;
    private String password;
    private String tokenUID; // Уникальный идентификатор, используется для доступа к пользователь в бд
    private String location;
    private String bio;     // Краткое описание профиля, как в инстаграмме

    private final ArrayList<String> followers = new ArrayList<>();    // Id подписчики
    private final ArrayList<String> following = new ArrayList<>();    // Id подписок

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void addFollower(User user) {
        followers.add(user.getTokenUID());
    }

    public void addFollowing(User user) {
        followers.add(user.getTokenUID());
    }

    public void removeFollower(User user) {
        followers.remove(user.getTokenUID());
    }

    public void removeFollowing(User user) {
        followers.remove(user.getTokenUID());
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

}
