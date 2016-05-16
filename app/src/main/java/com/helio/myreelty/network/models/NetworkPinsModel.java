package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.models.PinModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fedir on 04.04.2016.
 */
public class NetworkPinsModel {

    @SerializedName("pins")
    @Expose
    public List<PinModel> pins = new ArrayList<PinModel>();

    public List<PinModel> getPins() {
        return pins;
    }

    public void setPins(List<PinModel> pins) {
        this.pins = pins;
    }
}
