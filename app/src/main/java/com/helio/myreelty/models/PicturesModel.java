package com.helio.myreelty.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Fedir on 10.03.2016.
 */
public class PicturesModel implements Parcelable {
    @SerializedName("sizes")
    @Expose
    public SizeModel[] sizes = new SizeModel[4];

    public static final Creator<PicturesModel> CREATOR = new Creator<PicturesModel>() {
        @Override
        public PicturesModel createFromParcel(Parcel in) {
            return new PicturesModel(in);
        }

        @Override
        public PicturesModel[] newArray(int size) {
            return new PicturesModel[size];
        }
    };

    protected PicturesModel(Parcel in) {
        in.readTypedList(new ArrayList<>(), SizeModel.CREATOR);
    }

    public SizeModel[] getSizes() {

        return sizes;
    }

    public void setSizes(SizeModel[] sizes) {
        this.sizes = sizes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(sizes);
    }

}
