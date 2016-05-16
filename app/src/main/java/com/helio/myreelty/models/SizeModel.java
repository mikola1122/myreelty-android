package com.helio.myreelty.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fedir on 10.03.2016.
 */
public class SizeModel implements Parcelable {
    @SerializedName("width")
    @Expose
    public Integer width;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("height")
    @Expose
    public Integer height;

    protected SizeModel(Parcel in) {
        link = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    public static final Creator<SizeModel> CREATOR = new Creator<SizeModel>() {
        @Override
        public SizeModel createFromParcel(Parcel in) {
            return new SizeModel(in);
        }

        @Override
        public SizeModel[] newArray(int size) {
            return new SizeModel[size];
        }
    };

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(link);
        dest.writeInt(height);
        dest.writeInt(width);
    }
}
