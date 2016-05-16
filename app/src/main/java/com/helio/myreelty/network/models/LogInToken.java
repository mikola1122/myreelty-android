package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fedir on 25.02.2016.
 */
public class LogInToken {


    @SerializedName("authentication_token")
    @Expose
    public AuthenticationToken authenticationToken;


    public String getAuthenticationToken() {
        return authenticationToken.getContent();
    }

    public void setAuthenticationToken(AuthenticationToken authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
