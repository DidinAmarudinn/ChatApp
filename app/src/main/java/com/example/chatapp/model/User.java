package com.example.chatapp.model;

public class User {
    private String username;
    private String id;
    private String imageUrl;
    private String email;
    private String bio;
    private String address;
    private String phonenumber;
    private String status;
    private String search;


    public User(String username, String id, String imageUrl, String email, String bio, String address, String phonenumber,String status,String search) {
        this.username = username;
        this.id = id;
        this.imageUrl = imageUrl;
        this.email = email;
        this.bio = bio;
        this.address = address;
        this.phonenumber = phonenumber;
        this.status=status;
        this.search=search;
    }

    public User() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
