package com.meeting.management.model;

import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {

    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



}
