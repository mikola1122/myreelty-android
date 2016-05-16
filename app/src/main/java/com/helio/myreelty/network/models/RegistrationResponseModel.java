package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fedir on 25.02.2016.
 */
public class RegistrationResponseModel {


    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("created_at")
    @Expose
    public String createdAt;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
