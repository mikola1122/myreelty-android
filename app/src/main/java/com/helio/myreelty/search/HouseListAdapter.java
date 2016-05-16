package com.helio.myreelty.search;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.helio.myreelty.MyApplication;
import com.helio.myreelty.R;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.common.dialog.HouseDialogFragment;
import com.helio.myreelty.common.pagination.PaginationRecyclerAdapter;
import com.helio.myreelty.databinding.ItemSearchedHouseBinding;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.video_player.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fedir on 19.02.2016.
 */
public class HouseListAdapter extends PaginationRecyclerAdapter {

    private List<HouseModel> mHouseList;
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private boolean allItemsLoaded;


    public HouseListAdapter(Activity context, List<HouseModel> houses, RecyclerView recyclerView) {
        mHouseList = houses;
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SearchedHouseHolder(DataBindingUtil.inflate(inflater, R.layout.item_searched_house, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchedHouseHolder houseHolder = (SearchedHouseHolder) holder;
        HouseModel houseModel = mHouseList.get(position);
        houseHolder.binding.setSearchedVideos(houseModel);
    }

    @Override
    public int getItemCount() {
            return mHouseList.size();
    }

    public class SearchedHouseHolder extends RecyclerView.ViewHolder {

        private ItemSearchedHouseBinding binding;
        private View.OnClickListener clickListener = v ->
                openDialogWindow(mHouseList.get(mRecyclerView.getChildAdapterPosition(binding.getRoot())));

        public SearchedHouseHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(HouseListAdapter.this::openVideoActivity);
            itemView.findViewById(R.id.btn_item_house_menu).setOnClickListener(clickListener);
        }

        private String getTitle() {
            FontTextView textView = (FontTextView) binding.getRoot().findViewById(R.id.searched_house_full_address);
            return textView.getText().toString();
        }

        private void openDialogWindow(HouseModel review) {
            DialogFragment fragment = HouseDialogFragment.newInstance(getTitle(), review);
            fragment.show(mContext.getFragmentManager(), "dialog fragment");
        }
    }

    private void openVideoActivity(View v) {
        MyApplication.houseSingleton.setCurrentHouse(mHouseList.get(mRecyclerView.getChildLayoutPosition(v)));
        HouseSingleton.getInstance().setUser(mHouseList.get(mRecyclerView.getChildLayoutPosition(v)).getUser());
        Intent intent = new Intent(mContext, VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    public void updateHouseList(List<HouseModel> houses) {
        mHouseList = houses;
        notifyDataSetChanged();
    }

    public void addHouseList(List<HouseModel> houses){
        mHouseList.addAll(houses);
        notifyItemInserted(mHouseList.size());
    }

    public void addNewItems(List<HouseModel> items) {
        if (items.size() == 0) {
            allItemsLoaded = true;
            return;
        }
        mHouseList.addAll(items);
    }

    public boolean isAllItemsLoaded() {
        return allItemsLoaded;
    }


    @BindingAdapter({"app:imageUrl1"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(ContextCompat.getDrawable(view.getContext(), R.mipmap.placeholder_home))
        .into(view);
    }
}
