package com.helio.myreelty.user_profile;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.helio.myreelty.R;
import com.helio.myreelty.common.AppConstant;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.common.pagination.PaginationTool;
import com.helio.myreelty.databinding.FragmentUserProfileBinding;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.NetworkHouseModel;
import com.helio.myreelty.search.HouseListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Taras on 05.04.2016.
 */
public class UserProfileFragment extends Fragment {

    private HouseListAdapter adapter;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        FragmentUserProfileBinding binding = DataBindingUtil.bind(view);
        binding.setUser(HouseSingleton.getInstance().getUser());
        initRecyclerView(view);
        return view;
    }

    @BindingAdapter({"app:imageSrc"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .resize(150,150)
                .centerCrop()
                .onlyScaleDown()
                .placeholder(ContextCompat.getDrawable(view.getContext(), R.drawable.profile_male))
                .into(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.user_profile_video_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setNestedScrollingEnabled(true);
        adapter = new HouseListAdapter(getActivity(), new ArrayList<>(), recyclerView);
        recyclerView.setAdapter(adapter);
        addPagination(adapter, recyclerView);
    }

    private void addPagination(HouseListAdapter adapter, RecyclerView recyclerView) {

        PaginationTool<NetworkHouseModel> paginationTool = PaginationTool
                .buildPagingObservable(recyclerView, adapter, offset -> RetrofitBuilder.createRetrofitService(Api.class).getUsersReviews(HouseSingleton.getInstance().getUser().getId(), offset))
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
                        initRecyclerToolbar(items.getPagination().getTotalEntries());
                    }
                });
    }

    private void initRecyclerToolbar(int reviewsCount) {
        ((FontTextView)getActivity().findViewById(R.id.user_profile_video_count))
                .setText(reviewsCount + " " + getResources().getString(R.string.videos));
    }
}
