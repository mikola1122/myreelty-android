package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Oleh on 3/3/2016.
 */
public class BookmarkId {

    @SerializedName("review_id")
    @Expose
    public Integer reviewId;

    public BookmarkId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }
}
