package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.video_player.util.DateConvector;

/**
 * Created by markus on 10.02.2016.
 */
public class UserModel {

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
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("avatar_url")
    @Expose
    public String avatar_url;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

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
        return DateConvector.formatDateFromString(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserModel(String avatar_url, String description, String phone, String createdAt, String email, Integer id, String name) {
        this.avatar_url = avatar_url;
        this.description = description;
        this.phone = phone;
        this.createdAt = createdAt;
        this.email = email;
        this.id = id;
        this.name = name;
    }

    public UserModel(String name, Integer id, String email, String createdAt) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UserModel() {
    }
}
