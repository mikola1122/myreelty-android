package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolas on 29.03.2016.
 */
public class CommentContentModel {
    @SerializedName("content")
    @Expose
    public String content;

    public CommentContentModel(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
