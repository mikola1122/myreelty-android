package com.helio.myreelty.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fedir on 01.04.2016.
 */
public class NetworkUserModel {

    @SerializedName("account")
    public UserModel account;

    public UserModel getAccount() {
        return account;
    }

    public void setAccount(UserModel account) {
        this.account = account;
    }
}
