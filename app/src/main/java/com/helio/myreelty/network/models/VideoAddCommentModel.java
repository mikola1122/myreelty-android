package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolas on 29.03.2016.
 */
public class VideoAddCommentModel {
    @SerializedName("comment")
    @Expose
    public CommentContentModel commentModel;

    public VideoAddCommentModel(CommentContentModel commentModel) {
        this.commentModel = commentModel;
    }

    public CommentContentModel getCommentModel() {
        return commentModel;
    }

    public void setCommentModel(CommentContentModel commentModel) {
        this.commentModel = commentModel;
    }
}
