package com.helio.myreelty.video_player.likes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helio.myreelty.R;
import com.helio.myreelty.common.AppConstant;
import com.helio.myreelty.common.pagination.PaginationTool;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.LikeModel;
import com.helio.myreelty.network.models.NetworkLikeModel;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Taras on 31.03.2016.
 */
public class LikesFragment extends Fragment {


    private Context mContext;
    private RecyclerView recyclerView;
    private LikesAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_likes, container, false);
        initRecyclerView(view);
        initAdapter(recyclerView);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.likes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setNestedScrollingEnabled(true);
    }

    public LikesAdapter getAdapter() {
        return adapter;
    }

    private void initAdapter(RecyclerView recyclerView) {
        ArrayList<LikeModel> mLikes = new ArrayList<>();
        adapter = new LikesAdapter(mLikes, getActivity());
        recyclerView.setAdapter(adapter);
        addPagination(adapter, recyclerView);
    }

    private void addPagination(LikesAdapter adapter, RecyclerView recyclerView) {
        PaginationTool<NetworkLikeModel> paginationTool = PaginationTool
                .buildPagingObservable(recyclerView, adapter, offset ->
                        RetrofitBuilder.createRetrofitService(Api.class).getLikes(HouseSingleton.getInstance().getCurrentHouse().getId()))
                .setLimit(AppConstant.PAGING_LIMIT)
                .setRetryCount(3)
                .build();

        paginationTool.getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NetworkLikeModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(NetworkLikeModel items) {
                        adapter.setMaxPage(items.getPagination().getTotalPages());
                        adapter.addItems(items.getLikes());
                    }
                });
    }
}
