package com.helio.myreelty.navigation.bookmark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.helio.myreelty.R;
import com.helio.myreelty.common.AppConstant;
import com.helio.myreelty.common.User;
import com.helio.myreelty.common.pagination.PaginationTool;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.BookmarkHouseModel;
import com.helio.myreelty.network.models.BookmarkModel;
import com.helio.myreelty.search.HouseListAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Taras on 3/1/2016.
 */
public class BookmarkFragment extends Fragment {

    private LinearLayout emptyBookmarksLayout;
    private List<HouseModel> houseModels = new ArrayList<>();
    private HouseListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        initRecycler(view);
        emptyBookmarksLayout = (LinearLayout) view.findViewById(R.id.pic_empty_bookmarks);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!houseModels.isEmpty()) {
            emptyBookmarksLayout.setVisibility(View.INVISIBLE);
        }else {
            emptyBookmarksLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initRecycler(View view) {
        RecyclerView searchedVideoList = (RecyclerView) view.findViewById(R.id.recycler_bookmarks);
        searchedVideoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HouseListAdapter(getActivity(), houseModels, searchedVideoList);
        searchedVideoList.setAdapter(adapter);
        addPagination(adapter, searchedVideoList);
    }

    private void addPagination(HouseListAdapter adapter, RecyclerView recyclerView) {
        PaginationTool<BookmarkHouseModel> paginationTool = PaginationTool
                .buildPagingObservable(recyclerView, adapter, offset ->
                        RetrofitBuilder.createRetrofitService(Api.class).getBookmarks(User.getToken(getContext()),offset))
                .setLimit(AppConstant.PAGING_LIMIT)
                .setRetryCount(3)
                .build();

        paginationTool.getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookmarkHouseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BookmarkHouseModel items) {
                        adapter.setMaxPage(items.getPagination().getTotalPages());
                        updateBookmarksList(items);
                    }
                });
    }

    private void updateBookmarksList(BookmarkHouseModel networkHousesModel) {
        for (BookmarkModel bookmark : networkHousesModel.getBookmarks()){
            houseModels.add(bookmark.getReview());
        }
        adapter.updateHouseList(houseModels);
        if (!networkHousesModel.getBookmarks().isEmpty()) {
            emptyBookmarksLayout.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            emptyBookmarksLayout.setVisibility(View.VISIBLE);
        }
    }
}
