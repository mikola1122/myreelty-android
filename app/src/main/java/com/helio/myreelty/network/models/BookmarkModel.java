package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.models.HouseModel;

/**
 * Created by Taras on 29.03.2016.
 */
public class BookmarkModel {

    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("review_id")
    @Expose
    public Integer reviewId;
    @SerializedName("review")
    @Expose
    public HouseModel review = new HouseModel();

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    @SerializedName("id")
    @Expose

    public Integer id;
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    public HouseModel getReview() {
        return review;
    }

    public void setReview(HouseModel review) {
        this.review = review;
    }
}
