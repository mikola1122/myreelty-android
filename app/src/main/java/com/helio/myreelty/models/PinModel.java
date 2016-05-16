package com.helio.myreelty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fedir on 04.04.2016.
 */
public class PinModel {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("location")
    @Expose
    public double [] location = new double[2];


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}
