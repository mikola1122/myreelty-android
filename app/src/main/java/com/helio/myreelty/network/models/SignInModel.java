package com.helio.myreelty.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by markus on 10.02.2016.
 */
public class SignInModel {


    @SerializedName("session")
    private SessionModel sessionModel;

    public SignInModel(String email, String password) {
        this.sessionModel = new SessionModel(email, password);
    }

    public SessionModel getSessionModel() {
        return sessionModel;
    }

    public void setSessionModel(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }
}
