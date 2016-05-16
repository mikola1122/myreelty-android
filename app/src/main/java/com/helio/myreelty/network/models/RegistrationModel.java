package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by markus on 10.02.2016.
 */
public class RegistrationModel {

    @SerializedName("account")
    @Expose
    public AccountModel account;


    public RegistrationModel(String email, String password, String name) {
        this.account = new AccountModel(email, password, name);
    }

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }
}


