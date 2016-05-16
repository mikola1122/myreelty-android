package com.helio.myreelty.network.models;

import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.models.HouseModel;

/**
 * Created by Fedir on 04.04.2016.
 */
public class NetworkReviewModel {

    @SerializedName("review")
    public HouseModel house;

    public HouseModel getHouse() {
        return house;
    }

    public void setHouse(HouseModel house) {
        this.house = house;
    }
}
