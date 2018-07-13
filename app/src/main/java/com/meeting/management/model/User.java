package com.meeting.management.model;

import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("userId")
    private String userId;
    @SerializedName("name")
    private String name;

    private String email;

    public User(String email) {
        this.setEmail(email);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
