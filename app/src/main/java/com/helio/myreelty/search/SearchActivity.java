package com.helio.myreelty.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.helio.myreelty.R;
import com.helio.myreelty.R.layout;
import com.helio.myreelty.common.custom_view.FontEditText;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.NetworkHouseModel;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by markus on 17.02.2016.
 */
@SuppressWarnings("unchecked")
public class SearchActivity extends AppCompatActivity {

    private static final String MAP_FRAGMENT_TAG = "map fragment tag";
    private static final String SEARCH_VIDEO_FRAGMENT_TAG = "search video fragment tag";
    private ImageView changeContentButton;
    private boolean mapIsOpened = true;
    private List<HouseModel> mHouses = new ArrayList<>();
    private FontEditText etSearch;
    private CameraPosition cameraPosition;
    private int currentPage = 1;
    private int lastPage = 1;

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.content_change:
                changeContent();
                break;
            case R.id.search_clear:
                clearSearch();
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            default:
                break;
        }
    };

    private void clearSearch() {
        etSearch.setText("");
    }

    //TODO remove hardcoded values
    private void initCameraPosition(){
            cameraPosition = new CameraPosition(new LatLng(40.209971, -95.647420), 3.1f, 0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_search);
        initETSearch();
        setupToolBar();
        openSearchMapFragment();
        initCameraPosition();
    }

//    private void initObservable() {
//
//      Observable observable =  Observable.create(new Observable.OnSubscribe<Integer>(){
//
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                subscriber.onNext(currentPage);
//            }
//        });
//        observable.subscribe();
//
//         observable = RetrofitBuilder
//                .createRetrofitService(Api.class)
//                .getAllHouses(currentPage);
//
//
//        observable.doOnNext(o -> {
//            currentPage ++;
//            if(currentPage > lastPage){
//                observable.doOnCompleted(() -> {
//
//                });
//            }
//
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadNextPage();
    }

    private void initETSearch() {
        etSearch = (FontEditText) findViewById(R.id.et_search_house);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest(etSearch.getText().toString());
                HouseSingleton.getInstance().setSearchAddress(etSearch.getText().toString());
                handled = true;
            }
            return handled;
        });
    }

    private void sendRequest(String value) {
        if(!TextUtils.isEmpty(value)) {
            hideSnackBar();
            getAllHousesByAddress(value);
        }
    }

    private void hideSnackBar(){
        MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        if(fragment.snackbar != null && fragment.snackbar.isShown()) {
            fragment.snackbar.dismiss();
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_activity_toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.search_clear).setOnClickListener(onClickListener);
        changeContentButton = (ImageView) findViewById(R.id.content_change);
        findViewById(R.id.back_button).setOnClickListener(onClickListener);
        changeContentButton.setOnClickListener(onClickListener);
    }

    private void changeContent() {
        if (!mapIsOpened) {
            changeContentButton.setImageResource(R.mipmap.ic_list_white_24_px);
            openSearchMapFragment();
            mapIsOpened = true;
        } else {
            changeContentButton.setImageResource(R.mipmap.ic_map_white_24_px);
            openSearchVideoFragment();
            mapIsOpened = false;
        }
    }

    private void openSearchVideoFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.search_container, new SearchVideoFragment(), SEARCH_VIDEO_FRAGMENT_TAG)
                .commit();
    }

    private void openSearchMapFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_container, new MapFragment(), MAP_FRAGMENT_TAG)
                .commit();
    }

    private void getAllHousesByAddress(String address) {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .getReviewsNear(address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkHouseModel -> {
                    updateFragment(networkHouseModel, true);
                }, Throwable::printStackTrace);
    }

//    public void loadNextPage() {
//
//        if(currentPage <= lastPage) {
//            RetrofitBuilder
//                    .createRetrofitService(Api.class)
//                    .getAllHouses(currentPage)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(networkHouseModel -> {
//                        addHousesToFragments(networkHouseModel);
//                        loadNextPage();
//                    }, Throwable::printStackTrace);
//        }
//    }

    private void updateFragment(NetworkHouseModel searchedHouse, boolean zoomToLast) {
        hideKeyboard();
        currentPage += 1;
        lastPage = searchedHouse.getPagination().getTotalPages();
        mHouses = searchedHouse.getReviews();
//        updateMapFragment(searchedHouse, zoomToLast);
        updateSearchVideoFragment(searchedHouse);
    }
//
//    private void addHousesToFragments(NetworkHouseModel searchedHouse){
//        currentPage += 1;
//        lastPage = searchedHouse.getPagination().getTotalPages();
//        mHouses.addAll(searchedHouse.getReviews());
//        addHousesToMapFragment(searchedHouse);
//        addHousesToVideoFragment(searchedHouse);
//    }

    private void updateSearchVideoFragment(NetworkHouseModel searchedHouse) {
        SearchVideoFragment fragment = (SearchVideoFragment) getFragmentManager().findFragmentByTag(SEARCH_VIDEO_FRAGMENT_TAG);
        if(fragment != null) {
            fragment.updateHousesList(searchedHouse.getReviews());
        }
    }

    private void addHousesToVideoFragment(NetworkHouseModel searchedHouse){
        SearchVideoFragment fragment = (SearchVideoFragment) getFragmentManager().findFragmentByTag(SEARCH_VIDEO_FRAGMENT_TAG);
        if(fragment != null) {
            fragment.addHousesList(searchedHouse.getReviews());
        }
    }

//    private void updateMapFragment(NetworkHouseModel searchedHouse, boolean zoomToLast) {
//        MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
//        if(fragment != null) {
//            fragment.clearMap();
//            fragment.updateHouses(searchedHouse.getReviews());
//            fragment.addLocations(searchedHouse.getReviews());
//            fragment.setUpMap(searchedHouse.getReviews());
//            if (zoomToLast)
//                fragment.moveCameraToLastMarker();
//        }
//    }

//    private void addHousesToMapFragment(NetworkHouseModel searchedHouse){
//        MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
//        if(fragment != null) {
//            fragment.addHouses(searchedHouse.getReviews());
//            fragment.addLocations(searchedHouse.getReviews());
//            fragment.setUpMap(searchedHouse.getReviews());
//        }
//    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    public List<HouseModel> getHouses() {
        return mHouses;
    }

    public void setCameraPosition(CameraPosition cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    public CameraPosition getCameraPosition(){
        return cameraPosition;
    }
}
