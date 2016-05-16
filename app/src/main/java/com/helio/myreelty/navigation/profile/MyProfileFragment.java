package com.helio.myreelty.navigation.profile;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.helio.myreelty.R;
import com.helio.myreelty.common.AppConstant;
import com.helio.myreelty.common.User;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.common.pagination.PaginationTool;
import com.helio.myreelty.databinding.FragmentMyProfileBinding;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.NetworkHouseModel;
import com.helio.myreelty.search.HouseListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Fedir on 03.03.2016.
 */
public class MyProfileFragment extends Fragment {

    private HouseListAdapter adapter;

    private View.OnClickListener menuClickListener() {
        return v -> initPopupMenu(v);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        FragmentMyProfileBinding binding = DataBindingUtil.bind(view);
        binding.setUser(User.getUser(getActivity()));
        initRecyclerView(view);
        initLogOut(view);
        return view;
    }

    @BindingAdapter({"app:imageSrc"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(ContextCompat.getDrawable(view.getContext(), R.drawable.profile_male))
                .resize(50,50)
                .into(view);
    }

    private void initLogOut(View view) {
        view.findViewById(R.id.my_profile_menu).setOnClickListener(menuClickListener());
    }

    private void initPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popupmenu);
        popupMenu.setOnMenuItemClickListener(item -> {
            logOut(v.getContext());
            return true;
        });
        popupMenu.setOnDismissListener(menu -> {});
        popupMenu.show();
    }

    private void logOut(Context context) {
        User.clearUser(context);
        getActivity().onBackPressed();
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_profile_video_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(true);
        adapter = new HouseListAdapter(getActivity(), new ArrayList<>(), recyclerView);
        recyclerView.setAdapter(adapter);
        addPagination(adapter, recyclerView);
    }

    private void addPagination(HouseListAdapter adapter, RecyclerView recyclerView) {

        PaginationTool<NetworkHouseModel> paginationTool = PaginationTool
                .buildPagingObservable(recyclerView, adapter, offset -> RetrofitBuilder.createRetrofitService(Api.class).getAccountReviews(User.getToken(getActivity()),offset))
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

    private void updateList(NetworkHouseModel networkHouseModel) {
        adapter.addHouseList(networkHouseModel.getReviews());
        initRecyclerToolbar(networkHouseModel.getPagination().getTotalEntries());
    }

    private void initRecyclerToolbar(int reviewsCount) {
        FontTextView textView = (FontTextView)getActivity().findViewById(R.id.my_profile_video_list_size);
        textView.setText(reviewsCount + " " + getResources().getString(R.string.current_user_videos));
    }
}
