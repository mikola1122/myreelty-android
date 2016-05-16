package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.models.HouseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fedir on 10.03.2016.
 */
public class NetworkHouseModel {

    @SerializedName("reviews")
    @Expose
    public List<HouseModel> reviews = new ArrayList<>();
    @SerializedName("pagination")
    public PaginationModel pagination;

    public List<HouseModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<HouseModel> reviews) {
        this.reviews = reviews;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    public void setPagination(PaginationModel pagination) {
        this.pagination = pagination;
    }
}
