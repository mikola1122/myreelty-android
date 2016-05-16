package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Nicolas on 09.03.2016.
 */
public class VideoCommentsModel {

    @SerializedName("comments")
    @Expose
    public ArrayList<NetworkCommentModel> networkCommentModels;

    @SerializedName("pagination")
    @Expose
    public PaginationModel pagination;

    public ArrayList<NetworkCommentModel> getCommentsModels() {
        return networkCommentModels;
    }

    public ArrayList<NetworkCommentModel> getNetworkCommentModels() {
        return networkCommentModels;
    }

    public void setNetworkCommentModels(ArrayList<NetworkCommentModel> networkCommentModels) {
        this.networkCommentModels = networkCommentModels;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    public void setPagination(PaginationModel pagination) {
        this.pagination = pagination;
    }
}
