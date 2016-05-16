package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolas on 09.03.2016.
 */
public class NetworkCommentModel {

    @SerializedName("user_id")
    @Expose
    public Integer userId;

    @SerializedName("user")
    @Expose
    public UserModel user;


    @SerializedName("review_id")
    @Expose
    public Integer reviewId;

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("content")
    @Expose
    public String content;

    public NetworkCommentModel(Integer userId, UserModel user , Integer reviewId, Integer id, String createdAt, String content) {
        this.userId = userId;
        this.user = user;
        this.reviewId = reviewId;
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
