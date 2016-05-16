package com.helio.myreelty.video_player.video_up_next;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helio.myreelty.R;
import com.helio.myreelty.common.User;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.navigation.home.UpNextRecyclerAdapter;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.search.HouseListAdapter;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Fedir on 23.02.2016.
 */
public class VideoUpNextFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_up_next, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.video_up_next_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(true);
        getAllHouses(recyclerView);
    }

    private void getAllHouses(RecyclerView  recyclerView) {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .getAllHouses(User.getToken(getContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(houses -> {
                    initAdapter(houses.getReviews(), recyclerView);
                }, Throwable::printStackTrace);
    }

    private void initAdapter(List<HouseModel> houses, RecyclerView recyclerView) {
        UpNextRecyclerAdapter adapter = new UpNextRecyclerAdapter(recyclerView, getActivity(), houses);
        recyclerView.setAdapter(adapter);
    }
}