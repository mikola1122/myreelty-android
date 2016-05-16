package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taras on 29.03.2016.
 */
public class BookmarkHouseModel {

    @SerializedName("bookmarks")
    @Expose
    public List<BookmarkModel> bookmarks = new ArrayList<>();
    @SerializedName("pagination")
    public PaginationModel pagination;

    public List<BookmarkModel> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<BookmarkModel> reviews) {
        this.bookmarks = reviews;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    public void setPagination(PaginationModel pagination) {
        this.pagination = pagination;
    }

}
