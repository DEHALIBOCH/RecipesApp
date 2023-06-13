package com.demoapp.recipesapp.data;

import java.util.ArrayList;

public class User {
    private String name;
    private String lastname;
    private int age;
    private String email;
    private String password;
    private String tokenUID; // Уникальный идентификатор

    private String avatarUrl; // Url аватара

    private String firebaseKey;
    private String location;
    private String bio;     // Краткое описание профиля, как в инстаграмме

    private final ArrayList<String> followers = new ArrayList<>();    // Id подписчики
    private final ArrayList<String> following = new ArrayList<>();    // Id подписок

    public void addRecipe(Recipe recipe) {

    }

    public ArrayList<String> getRecipes() {
        return recipes;
    }

    public ArrayList<String> getSavedRecipes() {
        return savedRecipes;
    }

    private final ArrayList<String> recipes = new ArrayList<>();    // Id выложенных рецептов
    private final ArrayList<String> savedRecipes = new ArrayList<>(); // Id сохраненных рецептов

    public User() {
    }

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

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    @Override
    public String toString() {
        return "User{" + "\n" +
                "name='" + name + '\'' + "\n" +
                ", lastname='" + lastname + '\'' + "\n" +
                ", age=" + age + "\n" +
                ", email='" + email + '\'' + "\n" +
                ", password='" + password + '\'' + "\n" +
                ", tokenUID='" + tokenUID + '\'' + "\n" +
                ", firebaseKey='" + firebaseKey + '\'' + "\n" +
                ", location='" + location + '\'' + "\n" +
                ", bio='" + bio + '\'' + "\n" + '}';
    }
}
