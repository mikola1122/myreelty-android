package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Taras on 31.03.2016.
 */
public class NetworkLikeModel {

    @SerializedName("pagination")
    @Expose
    public PaginationModel pagination;
    @SerializedName("likes")
    @Expose
    public List<LikeModel> likes;

    public NetworkLikeModel(PaginationModel pagination, List<LikeModel> likes) {
        this.pagination = pagination;
        this.likes = likes;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    public void setPagination(PaginationModel pagination) {
        this.pagination = pagination;
    }

    public List<LikeModel> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeModel> likes) {
        this.likes = likes;
    }
}
