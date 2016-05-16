package com.helio.myreelty.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.helio.myreelty.R;
import com.helio.myreelty.R.id;
import com.helio.myreelty.R.layout;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Taras on 3/10/2016.
 */
public class FilterActivity extends AppCompatActivity {

    private final int FIRST_ITEM = 0;
    private RadioGroup statusGroup;
    private RadioGroup bedsGroup;
    private RadioGroup bathsGroup;
    private Spinner spinner;
    private RangeSeekBar priceSeekBar;
    private RangeSeekBar squareSeekBar;
    private FiltersSettings filtersSettings;
    public List<HouseModel> houses;
    private final int ANY = 0;
    private final int HOUSE = 1;
    private final int CONDO = 2;
    private final int TOWNHOUSE = 3;

    private OnCheckedChangeListener checkedChangeListener = (RadioGroup group, int checkedId) -> {
    };

    private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_filter);
        filtersSettings = FiltersSettings.getInstance(this);
        setupToolBar();
        initSpinner();
        initRadioGroups();
        initRangeSeekBars();
        initValues();
        findViewById(id.btn_apply_filters).setOnClickListener(onClickListener);
    }

    private void initRadioGroups() {
        statusGroup = (RadioGroup) findViewById(id.filter_status_radio_group);
        statusGroup.setOnCheckedChangeListener(checkedChangeListener);

        bedsGroup = (RadioGroup) findViewById(id.filter_beds_radio_group);
        bedsGroup.setOnCheckedChangeListener(checkedChangeListener);

        bathsGroup = (RadioGroup) findViewById(id.filter_baths_radio_group);
        bathsGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    private void initRangeSeekBars() {
        priceSeekBar = (RangeSeekBar) findViewById(id.price_seek_bar);
        squareSeekBar = (RangeSeekBar) findViewById(id.square_seek_bar);
    }


    private void initSpinner() {
        spinner = (Spinner) findViewById(id.spinner);
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }


    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(id.filter_activity_toolbar);
        setSupportActionBar(toolbar);
        findViewById(id.filter_back_button).setOnClickListener(onClickListener);
        findViewById(id.reset_button).setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case id.filter_back_button:
                onBackPressed();
                break;
            case id.reset_button:
                resetValues();
                break;
            case id.btn_apply_filters:
                saveValues();
                getHouses();
                break;
        }
    };

    public void getHouses(){
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .applyFilters(HouseSingleton.getInstance().getSearchAddress()
                        , getRange()
                        , getPropertyType()
                        , getBeds()
                        , getBedsFrom()
                        , getBaths()
                        , isAvailability()
                        , getBathsFrom()
                        , getSquareFrom()
                        , getSquareTo()
                        , getPriceFrom()
                        , getPriceTo())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkHouseModel -> {
                    houses = networkHouseModel.getReviews();
                }, Throwable::printStackTrace);
    }

    private String getPropertyType(){
        switch (spinner.getSelectedItemPosition()){
            case ANY:
                return null;
            case HOUSE:
                return getString(R.string.house);
            case CONDO:
                return getString(R.string.condo);
            case TOWNHOUSE:
                return getString(R.string.townhouse);
            default:
                return getString(R.string.other);
        }
    }

    private Integer getRange(){
        return null;
    }

    private Integer getBeds(){
        return getRadioButtonValueFromTag(bedsGroup);
    }

    private Integer getBedsFrom(){
        return getRadioButtonMinValueFromTag(bedsGroup);
    }

    private Integer getBaths(){
       return getRadioButtonValueFromTag(bathsGroup);
    }

    private Integer getBathsFrom(){
        return getRadioButtonMinValueFromTag(bathsGroup);
    }

    private boolean isAvailability(){
        if(((RadioButton) findViewById(statusGroup.getCheckedRadioButtonId())).getText().toString().equals("On the market")){
            return true;
        }else return false;
    }

    private Integer getSquareFrom(){
        return squareSeekBar.getSelectedMinValue().intValue();
    }

    private Integer getSquareTo(){
        return squareSeekBar.getSelectedMaxValue().intValue();
    }

    private Integer getPriceFrom(){
        return priceSeekBar.getSelectedMinValue().intValue();
    }

    private Integer getPriceTo(){
        return priceSeekBar.getSelectedMaxValue().intValue();
    }

    private Integer getRadioButtonValueFromTag(RadioGroup radioGroup){
        switch(radioGroup.getCheckedRadioButtonId()){
            case id.beds_1:
                return 1;
            case id.baths_1:
                return 1;
            case id.beds_2:
                return 2;
            case id.baths_2:
                return 2;
            case id.beds_3:
                return 3;
            case id.baths_3:
                return 3;
            case id.beds_4:
                return 4;
            case id.baths_4:
                return 4;
            default:
                return 0;
        }
    }

    private Integer getRadioButtonMinValueFromTag(RadioGroup radioGroup){
        switch(radioGroup.getCheckedRadioButtonId()){
            case id.beds_any:
                return 0;
            case id.baths_any:
                return 0;
            case id.beds_5:
                return 5;
            case id.baths_5:
                return 5;
            default:
                return 0;
        }
    }

    private void resetValues() {
        ((RadioButton) statusGroup.getChildAt(FIRST_ITEM)).setChecked(true);
        ((RadioButton) bedsGroup.getChildAt(FIRST_ITEM)).setChecked(true);
        ((RadioButton) bathsGroup.getChildAt(FIRST_ITEM)).setChecked(true);
        spinner.setSelection(FIRST_ITEM);
        priceSeekBar.resetSelectedValues();
        squareSeekBar.resetSelectedValues();
    }

    private void saveValues() {
        filtersSettings.setBedsMinValue(getRadioButtonMinValueFromTag(bedsGroup));
        filtersSettings.setBedsValue(getRadioButtonValueFromTag(bedsGroup));
        filtersSettings.setBathsMinValue(getRadioButtonMinValueFromTag(bathsGroup));
        filtersSettings.setBathsValue(getRadioButtonValueFromTag(bathsGroup));
        filtersSettings.setStatusValue(isAvailability());
        filtersSettings.setPropertyTypeValue(getPropertyType());
        filtersSettings.setStatusButtonId(statusGroup.getCheckedRadioButtonId());
        filtersSettings.setBedsButtonId(bedsGroup.getCheckedRadioButtonId());
        filtersSettings.setBathsButtonId(bathsGroup.getCheckedRadioButtonId());
        filtersSettings.setSpinnerItemPosition(spinner.getSelectedItemPosition());
        filtersSettings.setPriceMinValue(priceSeekBar.getSelectedMinValue());
        filtersSettings.setPriceMaxValue(priceSeekBar.getSelectedMaxValue());
        filtersSettings.setSquareMinValue(squareSeekBar.getSelectedMinValue());
        filtersSettings.setSquareMaxValue(squareSeekBar.getSelectedMaxValue());
    }

    private void initValues() {
        if (!filtersSettings.isEmpty()) {
            loadValues();
        }else {
            resetValues();
        }
    }

    private void loadValues(){
        ((RadioButton) findViewById(filtersSettings.getStatusButtonId())).setChecked(true);
        ((RadioButton) findViewById(filtersSettings.getBedsButtonId())).setChecked(true);
        ((RadioButton) findViewById(filtersSettings.getBathsButtonId())).setChecked(true);
        spinner.setSelection(filtersSettings.getSpinnerItemPosition());
        priceSeekBar.setSelectedMinValue(filtersSettings.getPriceMinValue());
        priceSeekBar.setSelectedMaxValue(filtersSettings.getPriceMaxValue());
        squareSeekBar.setSelectedMinValue(filtersSettings.getSquareMinValue());
        squareSeekBar.setSelectedMaxValue(filtersSettings.getSquareMaxValue());
    }
}