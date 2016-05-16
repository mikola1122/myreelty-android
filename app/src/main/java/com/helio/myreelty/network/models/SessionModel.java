package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fedir on 25.02.2016.
 */
public class SessionModel {
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;

    public SessionModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public SessionModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
