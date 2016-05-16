package com.helio.myreelty.filter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.helio.myreelty.R;

/**
 * Created by Taras on 15.03.2016.
 */
public final class FiltersSettings {

    private static FiltersSettings instance;
    private static final String STATUS_GROUP = "statusGroup";
    private static final String BEDS_GROUP = "bedsGroup";
    private static final String BATHS_GROUP = "bathsGroup";
    private static final String SPINNER = "spinner";
    private static final String PRICE_MIN_VALUE = "priceMinValue";
    private static final String PRICE_MAX_VALUE = "priceMaxValue";
    private static final String SQUARE_MIN_VALUE = "squareMinValue";
    private static final String SQUARE_MAX_VALUE = "squareMaxValue";
    private static final String BEDS_MIN_VALUE = "bedsMinValue";
    private static final String BEDS_VALUE = "bedsValue";
    private static final String BATHS_MIN_VALUE = "bedsMinValue";
    private static final String BATHS_VALUE = "bedsValue";
    private static final String STATUS_VALUE = "statusValue";
    private static final String PROPERTY_TYPE_VALUE = "propertyTypeValue";
    private final int DEFAULT_VALUE = 0;
    public SharedPreferences mPref;
    private static Activity mContext;
    public SharedPreferences.Editor mEditor;

    private FiltersSettings(){
        mPref = mContext.getPreferences(Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    public boolean isEmpty(){
        if(mContext.getPreferences(Context.MODE_PRIVATE).getAll().isEmpty()){
            return true;
        }else
            return false;
    }

    public static FiltersSettings getInstance(Activity context){
        mContext = context;
        if(instance == null){
            instance = new FiltersSettings();
        }
        return instance;
    }

    public void setStatusButtonId(int buttonId){
        mEditor.putInt(STATUS_GROUP, buttonId);
        mEditor.commit();
    }

    public int getStatusButtonId(){
        return mPref.getInt(STATUS_GROUP, R.id.on_the_market);
    }

    public void setBedsButtonId(int buttonId){
        mEditor.putInt(BEDS_GROUP, buttonId);
        mEditor.commit();
    }

    public int getBedsButtonId(){
        return mPref.getInt(BEDS_GROUP, R.id.beds_any);
    }

    public void setBathsButtonId(int buttonId) {
        mEditor.putInt(BATHS_GROUP, buttonId);
        mEditor.commit();
    }

    public int getBathsButtonId(){
        return mPref.getInt(BATHS_GROUP, R.id.baths_any);
    }

    public void setSpinnerItemPosition(int itemPosition){
        mEditor.putInt(SPINNER, itemPosition);
        mEditor.commit();
    }

    public int getSpinnerItemPosition(){
        return mPref.getInt(SPINNER, DEFAULT_VALUE);
    }

    public void setPriceMinValue(Number minValue){
        mEditor.putInt(PRICE_MIN_VALUE, minValue.intValue());
        mEditor.commit();
    }

    public int getPriceMinValue(){
        return mPref.getInt(PRICE_MIN_VALUE, DEFAULT_VALUE);
    }

    public void setPriceMaxValue(Number maxValue){
        mEditor.putInt(PRICE_MAX_VALUE, maxValue.intValue());
        mEditor.commit();
    }

    public int getPriceMaxValue(){
        return mPref.getInt(PRICE_MAX_VALUE, DEFAULT_VALUE);
    }

    public void setSquareMinValue(Number minValue){
        mEditor.putInt(SQUARE_MIN_VALUE, minValue.intValue());
        mEditor.commit();
    }

    public int getSquareMinValue(){
        return mPref.getInt(SQUARE_MIN_VALUE, DEFAULT_VALUE);
    }

    public void setSquareMaxValue(Number maxValue){
        mEditor.putInt(SQUARE_MAX_VALUE, maxValue.intValue());
        mEditor.commit();
    }

    public int getSquareMaxValue(){
        return mPref.getInt(SQUARE_MAX_VALUE, DEFAULT_VALUE);
    }

    public void setBedsMinValue(Integer bedsMinValue) {
        mEditor.putInt(BEDS_MIN_VALUE, bedsMinValue);
        mEditor.commit();
    }

    public void setBedsValue(Integer bedsValue) {
        mEditor.putInt(BEDS_VALUE, bedsValue);
        mEditor.commit();
    }

    public void setBathsMinValue(Integer bathsMinValue) {
        mEditor.putInt(BATHS_MIN_VALUE, bathsMinValue);
        mEditor.commit();
    }

    public void setBathsValue(Integer bathsValue) {
        mEditor.putInt(BATHS_VALUE, bathsValue);
        mEditor.commit();
    }

    public void setStatusValue(boolean statusValue) {
        mEditor.putBoolean(STATUS_VALUE, statusValue);
        mEditor.commit();
    }

    public void setPropertyTypeValue(String propertyTypeValue) {
        mEditor.putString(PROPERTY_TYPE_VALUE, propertyTypeValue);
        mEditor.commit();
    }
}
