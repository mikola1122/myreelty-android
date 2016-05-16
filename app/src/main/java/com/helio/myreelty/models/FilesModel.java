package com.helio.myreelty.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolas on 06.04.2016.
 */
public class FilesModel {

    @SerializedName("quality")
    @Expose
    public String quality;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("width")
    @Expose
    public int width;
    @SerializedName("height")
    @Expose
    public int height;
    @SerializedName("link")
    @Expose
    public String link;

    public FilesModel(String quality, String type, int width, int height, String link) {
        this.quality = quality;
        this.type = type;
        this.width = width;
        this.height = height;
        this.link = link;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
