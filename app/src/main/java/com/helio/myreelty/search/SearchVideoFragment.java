package com.helio.myreelty.search;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.helio.myreelty.R.id;
import com.helio.myreelty.R.layout;
import com.helio.myreelty.common.AppConstant;
import com.helio.myreelty.common.pagination.PaginationTool;
import com.helio.myreelty.filter.FilterActivity;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.NetworkHouseModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by markus on 17.02.2016.
 */
public class SearchVideoFragment extends Fragment {

    private HouseListAdapter adapter;

    private OnClickListener clickListener = v -> startActivity(new Intent(getActivity(), FilterActivity.class));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_search_video, container, false);
        view.findViewById(id.fab_filter).setOnClickListener(clickListener);
        initRecycler(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateHouseList(((SearchActivity) getActivity()).getHouses());
    }

    private void initRecycler(View view) {
        RecyclerView searchedVideoList = (RecyclerView) view.findViewById(id.recycler_searched_video);
        searchedVideoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<HouseModel> houseModels = new ArrayList<>();
        adapter = new HouseListAdapter(getActivity(), houseModels, searchedVideoList);
        searchedVideoList.setAdapter(adapter);
        addPagination(adapter, searchedVideoList);
    }


    private void addPagination(HouseListAdapter adapter, RecyclerView recyclerView) {

        PaginationTool<NetworkHouseModel> paginationTool = PaginationTool
                .buildPagingObservable(recyclerView, adapter, offset -> RetrofitBuilder.createRetrofitService(Api.class).getAllHouses(offset))
                .setLimit(AppConstant.PAGING_LIMIT)
                .setRetryCount(3)
                .build();

        paginationTool.getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NetworkHouseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(NetworkHouseModel items) {
                        adapter.addHouseList(items.getReviews());
                        adapter.notifyItemInserted(adapter.getItemCount() - items.getReviews().size());
                        adapter.setMaxPage(items.getPagination().getTotalPages());
                    }
                });
    }

    public void updateHousesList(List<HouseModel> houses){
        adapter.updateHouseList(houses);
    }

    public void addHousesList(List<HouseModel> houses){
        adapter.addHouseList(houses);
    }
}
