package com.helio.myreelty.search.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Taras on 21.03.2016.
 */
public class MarkerItem implements ClusterItem{

    private final LatLng mPosition;
    public int imageRes;


    public MarkerItem(double lat, double lng, int imageRes) {
        this.imageRes = imageRes;
        mPosition = new LatLng(lat, lng);
    }

    public void setIcon(int resId){
        imageRes = resId;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }


}
