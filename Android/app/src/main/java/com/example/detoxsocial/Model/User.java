package com.example.detoxsocial.Model;

public class User {

    private String id;
    private String username;
    private String imageUrl;
    private String fullname;

    public User(String id, String username, String imageUrl, String fullname) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.fullname = fullname;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }
}
