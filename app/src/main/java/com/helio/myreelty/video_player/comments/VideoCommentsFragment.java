package com.helio.myreelty.video_player.comments;

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
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.VideoCommentsModel;
import com.helio.myreelty.video_player.VideoActivity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by Nicolas on 04.03.2016.
 */
public class VideoCommentsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_comments, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.video_comments_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(true);
        VideoCommentsAdapter commentsAdapter = new VideoCommentsAdapter(getActivity());
        recyclerView.setAdapter(commentsAdapter);
        addPagination(commentsAdapter, recyclerView);
    }

    private void addPagination(VideoCommentsAdapter adapter, RecyclerView recyclerView) {
        PaginationTool<VideoCommentsModel> paginationTool = PaginationTool
                .buildPagingObservable(recyclerView, adapter, offset ->
                        RetrofitBuilder.createRetrofitService(Api.class).getVideoComments(((VideoActivity) getActivity()).getHouse().getId(), offset))
                .setLimit(AppConstant.PAGING_LIMIT)
                .setRetryCount(3)
                .setEmptyListCount(1)
                .build();

        paginationTool.getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideoCommentsModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(VideoCommentsModel items) {
                        adapter.setMaxPage(items.getPagination().getTotalPages());
                        adapter.addComments(items);
                    }
                });
    }
}
