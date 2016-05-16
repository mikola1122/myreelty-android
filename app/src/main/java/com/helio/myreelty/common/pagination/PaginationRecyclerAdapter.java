package com.helio.myreelty.common.pagination;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Fedir on 30.03.2016.
 */
public abstract class PaginationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int maxPage = 1;
    private int currentPage = 0;

    public int getMaxPage(){
        return maxPage;
    }
    public void setMaxPage(int maxPage){
        this.maxPage  = maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
