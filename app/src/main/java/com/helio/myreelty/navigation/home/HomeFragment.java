package com.helio.myreelty.navigation.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helio.myreelty.R;
import com.helio.myreelty.common.AppConstant;
import com.helio.myreelty.common.pagination.PaginationTool;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.NetworkHouseModel;
import com.helio.myreelty.search.HouseListAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by markus on 11.02.2016.
 */
public class HomeFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initRecyclerView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void createAdapter(RecyclerView recyclerView, List<HouseModel> list) {
        HouseListAdapter adapter = new HouseListAdapter(getActivity(), list, recyclerView);
        recyclerView.setAdapter(adapter);
        addPagination(adapter, recyclerView);

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
                                adapter.addNewItems(items.getReviews());
                                adapter.notifyItemInserted(adapter.getItemCount() - items.getReviews().size());
                                adapter.setMaxPage(items.getPagination().getTotalPages());
                            }
                        });
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recent_top_reviews_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        ArrayList<HouseModel> list = new ArrayList<>();
        createAdapter(recyclerView, list);
    }
}