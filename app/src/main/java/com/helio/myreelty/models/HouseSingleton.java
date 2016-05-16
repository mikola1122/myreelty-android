package com.helio.myreelty.models;

import com.helio.myreelty.network.models.UserModel;

/**
 * Created by Taras on 28.03.2016.
 */
public class HouseSingleton {

    private HouseModel houseModel;
    private static HouseSingleton singleton;
    private long position = 0;
    private boolean isPlay = true;
    private String searchAddress;
    private UserModel user;

    public static HouseSingleton getInstance(){
        if (singleton == null) {
            singleton = new HouseSingleton();
        }
        return singleton;
    }

    public String getSearchAddress() {
        return searchAddress;
    }

    public void setSearchAddress(String searchAddress) {
        this.searchAddress = searchAddress;
    }

    private HouseSingleton() {
        houseModel = new HouseModel();
    }

    public HouseModel getCurrentHouse() {
        return houseModel;
    }

    public void setCurrentHouse(HouseModel houseModel) {
        this.houseModel = houseModel;
    }

    public long getVideoPosition() {
        return position;
    }

    public void setVideoPosition(long position) {
        this.position = position;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getUser() {
        return user;
    }
}
