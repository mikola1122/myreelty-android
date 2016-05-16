package com.helio.myreelty.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.helio.myreelty.R;
import com.helio.myreelty.R.id;
import com.helio.myreelty.R.layout;
import com.helio.myreelty.R.mipmap;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.filter.FilterActivity;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.models.PinModel;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.search.adapter.MyRenderer;
import com.helio.myreelty.search.model.MarkerItem;
import com.helio.myreelty.video_player.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Taras on 2/22/2016.
 */
public class MapFragment extends Fragment {

    private static final int MAGIC_NUMBER = 16;
    private final String DOLLAR_CHARACTER = "$ ";
    private static final int LONGITUDE = 1;
    private static final int LATITUDE = 0;
    private float ZOOM = 12.0f;
    private float ZOOMING = 2f;
    private HashMap<LatLng, Integer> markerList = new HashMap<>();
    private ArrayList<MarkerItem> markerItems = new ArrayList<>();
    private ClusterManager<MarkerItem> mClusterManager;
    private View mapView;
    public GoogleMap mMap;
    private MarkerItem mMarker;
    private boolean markerIsActive;
    public Snackbar snackbar;
    private Context mContext;
    private HouseModel mHouse;

    private OnClickListener openVideo = v -> openVideoActivity(mHouse);

    private OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case id.map_snackbar_menu_button:
                break;
            case id.map_fab_filter:
                startActivity(new Intent(mContext, FilterActivity.class));
                break;
            default:
                break;
        }
    };

    private ClusterManager.OnClusterClickListener clusterClickListener = new ClusterManager.OnClusterClickListener() {
        @Override
        public boolean onClusterClick(Cluster cluster) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), mMap.getCameraPosition().zoom + ZOOMING));
            return true;
        }
    };

    private ClusterManager.OnClusterItemClickListener markerClickListener = new ClusterManager.OnClusterItemClickListener() {
        @Override
        public boolean onClusterItemClick(ClusterItem clusterItem) {
            mMarker = (MarkerItem) clusterItem;
            //changeMarkerIcon(item);
            getHouse();
            return true;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mapView = inflater.inflate(layout.fragment_map, container, false);
        mapView.findViewById(id.map_fab_filter).setOnClickListener(onClickListener);
        createGoogleMapFragment();
        getAllPins();
        return mapView;
    }

    private void getAllPins() {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .getAllPins()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pins -> {
                   addLocations(pins.getPins());
                    setUpMap(pins.getPins());
                }, Throwable::printStackTrace);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void addLocations(List<PinModel> allPins) {
        if (allPins != null) {
            for (PinModel pin : allPins) {
                markerList.put(new LatLng(pin.getLocation()[LATITUDE], pin.getLocation()[LONGITUDE]), pin.getId());
            }
        }
    }

    private void createSnackBar(HouseModel house) {
        snackbar = Snackbar.make(mapView, "", Snackbar.LENGTH_INDEFINITE);
        setupLayoutParams(house);
        snackbar.setCallback(changeMarkerCallback);
        snackbar.show();
    }

    private void setupLayoutParams(HouseModel house) {
        SnackbarLayout layout = (SnackbarLayout) snackbar.getView();
        initSnackBarItems(layout, house);
        layout.setBackgroundColor(Color.WHITE);
        LayoutParams params = (LayoutParams) layout.getLayoutParams();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAGIC_NUMBER, getResources().getDisplayMetrics());
        params.setMargins(-px, 0, 0, -5);
        layout.setLayoutParams(params);
    }


    private void initSnackBarItems(View view, HouseModel house) {
        view.findViewById(id.map_snackbar_menu_button).setOnClickListener(onClickListener);
        ImageView houseImage = (ImageView) view.findViewById(id.map_snackbar_house_image);
        houseImage.setOnClickListener(openVideo);
        loadSnackBarImage(houseImage, house);

        FontTextView houseName = (FontTextView) view.findViewById(id.map_snackbar_house_name);
        houseName.setOnClickListener(openVideo);
        houseName.setText(house.getFullAddress());

        FontTextView housePrice = (FontTextView) view.findViewById(id.map_snackbar_house_price);
        housePrice.setOnClickListener(openVideo);
        housePrice.setText(DOLLAR_CHARACTER + house.getPrice());
    }

    private void loadSnackBarImage(ImageView houseImage, HouseModel house) {
        Picasso.with(mContext)
                .load(house.pictures.sizes[1].link)
                .placeholder(ContextCompat.getDrawable(mContext, mipmap.placeholder_home))
                .into(houseImage);
    }


    private void getHouse() {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .getHouse(markerList.get(mMarker.getPosition()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(house -> {
                    mHouse = house.getHouse();
                    createSnackBar(house.getHouse());
                }, Throwable::printStackTrace);
    }

    private void openVideoActivity(HouseModel houseModel) {
        HouseSingleton.getInstance().setCurrentHouse(houseModel);
        HouseSingleton.getInstance().setUser(houseModel.getUser());
        mContext.startActivity(new Intent(mContext, VideoActivity.class));
    }

    private Callback changeMarkerCallback = new Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            if (markerIsActive) {
//                mMarker.setIcon(mipmap.ic_place_white_24_px_copy_3);
            }
            super.onDismissed(snackbar, event);
        }

        @Override
        public void onShown(Snackbar snackbar) {
//            mMarker.setIcon(R.mipmap.pin_active);
            markerIsActive = true;
            super.onShown(snackbar);
        }
    };

    public void createGoogleMapFragment() {
        if (mMap == null) {
            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map)).getMapAsync(googleMap -> {
                mMap = googleMap;
                mMap.getUiSettings().setMapToolbarEnabled(false);
                setupClusterManager();
                mMap.setOnCameraChangeListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
            });
        }
    }

    public void setupClusterManager() {
        mClusterManager = new ClusterManager<>(mContext, mMap);
        mClusterManager.setRenderer(new MyRenderer(getContext(), mMap, mClusterManager));
        mClusterManager.setOnClusterClickListener(clusterClickListener);
        mClusterManager.setOnClusterItemClickListener(markerClickListener);
    }

    public void setUpMap(List<PinModel> mHouses) {
        addMarkers(mHouses);
        moveCamera(((SearchActivity) mContext).getCameraPosition());
    }



    public void moveCamera(CameraPosition cameraPosition) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, cameraPosition.zoom));
    }

    private void addMarkers(List<PinModel> pins) {
        if (pins != null && pins.size() > 0) {
            for (PinModel pin : pins) {
                if (pin.getLocation() != null) {
                    markerItems.add(new MarkerItem(pin.getLocation()[LATITUDE], pin.getLocation()[LONGITUDE], mipmap.ic_place_white_24_px_copy_3));
                }
            }
        }
        if (markerItems.size() > 0) {
            mClusterManager.addItems(markerItems);
        }
    }

    private void changeMarkerIcon(MarkerItem marker) {
        if (mMarker != null) {
            marker.setIcon(mipmap.pin_active);
            markerIsActive = false;
        } else {
            marker.setIcon(mipmap.ic_place_white_24_px_copy_3);
            markerIsActive = true;
        }
    }

    public void moveCameraToLastMarker(PinModel pin) {
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng((pin.getLocation()[LATITUDE]), pin.getLocation()[LONGITUDE]), ZOOM));
    }


    public void clearMap() {
        mMap.clear();
        markerItems.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((SearchActivity) mContext).setCameraPosition(mMap.getCameraPosition());
    }

}