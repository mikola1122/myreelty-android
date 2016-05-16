package com.helio.myreelty.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.helio.myreelty.network.models.UserModel;

/**
 * Created by Fedir on 29.02.2016.
 */
public class User {

    private static final String SETTINGS = "settings";
    private static final String USER_TOKEN = "user token";

    private static SharedPreferences sharedPreferences;


    public static boolean isLogedIn(Context context){
        return !TextUtils.isEmpty(getToken(context));
    }

    public static String getToken(Context context){
     sharedPreferences  =  context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_TOKEN, "");
    }

    public static void setToken(Context context, String token){
        sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(USER_TOKEN, token).apply();
    }

    public static void setUser(Context context, UserModel user){
        sharedPreferences  =  context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("UserModel", json).apply();
    }

    public static UserModel getUser(Context context){
        sharedPreferences  =  context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserModel", "");
        return gson.fromJson(json, UserModel.class);
    }

    public static void clearUser(Context context) {
        setToken(context, "");
        setUser(context, null);
    }
}
