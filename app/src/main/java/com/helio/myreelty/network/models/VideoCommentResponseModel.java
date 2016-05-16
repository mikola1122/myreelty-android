package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolas on 29.03.2016.
 */
public class VideoCommentResponseModel {
    @SerializedName("comment")
    @Expose
    public NetworkCommentModel networkCommentModels;

    public NetworkCommentModel getNetworkCommentModels() {
        return networkCommentModels;
    }
}
