package com.helio.myreelty.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fedir on 23.03.2016.
 */
public class PaginationModel {
    @SerializedName("total_pages")
    @Expose
    public Integer totalPages;
    @SerializedName("total_entries")
    @Expose
    public Integer totalEntries;
    @SerializedName("page_size")
    @Expose
    public Integer pageSize;
    @SerializedName("page_number")
    @Expose
    public Integer pageNumber;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(Integer totalEntries) {
        this.totalEntries = totalEntries;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
