package com.meeting.management.model;

import com.google.gson.annotations.SerializedName;

public class AuthenticationResponse {

    @SerializedName("user")
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
