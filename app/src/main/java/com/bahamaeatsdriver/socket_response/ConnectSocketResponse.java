package com.bahamaeatsdriver.socket_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectSocketResponse {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("socket_id")
    @Expose
    private String socketId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

}
