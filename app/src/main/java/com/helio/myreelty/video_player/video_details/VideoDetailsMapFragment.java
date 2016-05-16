package com.helio.myreelty.video_player.video_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helio.myreelty.R;
import com.helio.myreelty.R.mipmap;
import com.helio.myreelty.models.HouseModel;

/**
 * Created by Taras on 3/7/2016.
 */
public class VideoDetailsMapFragment extends Fragment {

    private GoogleMap mMap;
    private float ZOOM = 12.0f;
    private LatLng location;
    private final int LATITUDE = 0;
    private final int LONGITUDE = 1;
    private static HouseModel mHouseModel;
    public static VideoDetailsMapFragment newInstance(HouseModel houseModel) {
        mHouseModel = houseModel;
        return new VideoDetailsMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initLocation();
        View mapView = inflater.inflate(R.layout.fragment_details_google_map, container, false);
        createMap();
        return mapView;
    }

    public void createMap() {
        if (mMap == null) {
          ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.details_google_map)).getMapAsync(googleMap -> {
              mMap = googleMap;
              setUpMap();
          });
        }
    }

    //TODO reamove
    private void initLocation(){
        if(mHouseModel.getLocation().length>0 && mHouseModel.getLocation()!=null) {
            location = new LatLng(mHouseModel.getLocation()[LATITUDE],mHouseModel.getLocation()[LONGITUDE]);
        }
    }

    private void setUpMap() {
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM));
        addMarker();
    }

    private void addMarker(){
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromResource(mipmap.ic_place_white_24_px_copy_3)));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mMap != null) {
            setUpMap();
        }
        createMap();
    }
}