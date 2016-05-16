package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.video_player.util.DateConvector;

/**
 * Created by Taras on 31.03.2016.
 */
public class LikeModel {

    @SerializedName("user")
    @Expose
    public UserModel user;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    public LikeModel(UserModel user, Integer id, String createdAt) {
        this.user = user;
        this.id = id;
        this.createdAt = createdAt;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return DateConvector.formatDateFromString(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
