package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.models.HouseModel;

/**
 * Created by Taras on 25.03.2016.
 */

//TODO rename
public class HouseModelObject {

    @SerializedName("review")
    @Expose
    public HouseModel review = new HouseModel();

    public HouseModel getReview() {
        return review;
    }

    public void setReview(HouseModel review) {
        this.review = review;
    }
}
