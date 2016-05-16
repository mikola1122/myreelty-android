package com.helio.myreelty.navigation.home;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
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
import com.helio.myreelty.databinding.ItemUpNextBinding;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.video_player.VideoActivity;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by markus on 12.02.2016.
 */
public class UpNextRecyclerAdapter extends PaginationRecyclerAdapter {

    public static final int HOUSE_MODEL = 3;
    private boolean allItemsLoaded;


    private Activity mContext;
    private List<HouseModel> modelsList;
    private RecyclerView mRecyclerView;
    private ReviewHolder reviewHolder;

    public UpNextRecyclerAdapter(RecyclerView recyclerView, Activity context, List<HouseModel> modelsList) {
        this.modelsList = modelsList;
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder =  new ReviewHolder(DataBindingUtil.inflate(inflater, R.layout.item_up_next, parent, false).getRoot());
        return viewHolder;
    }

    private void openVideoActivity(int childAdapterPosition) {
        HouseModel house = modelsList.get(childAdapterPosition);
        HouseSingleton.getInstance().setCurrentHouse(house);
        HouseSingleton.getInstance().setUser(house.getUser());
        Intent intent = new Intent(mContext, VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                reviewHolder = (ReviewHolder) holder;
                HouseModel mostHouseModel = modelsList.get(position);
                reviewHolder.binding.setUpNext(mostHouseModel);
    }

    @Override
    public int getItemCount() {
        return modelsList.size();
    }


    @Override
    public int getItemViewType(int position) {
            return HOUSE_MODEL;
    }

    public void addHouses(List<HouseModel> houses){
        modelsList.addAll(houses);
        notifyItemInserted(getItemCount());
    }

    @BindingAdapter({"app:imageUrl1"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(ContextCompat.getDrawable(view.getContext(), R.mipmap.test_placeholder))
                .into(view);
    }

    public void addNewItems(List<HouseModel> items) {
        if (items.size() == 0) {
            allItemsLoaded = true;
            return;
        }
        modelsList.addAll(items);
    }

    public boolean isAllItemsLoaded() {
        return allItemsLoaded;
    }


    public class ReviewHolder extends RecyclerView.ViewHolder {

        private ItemUpNextBinding binding;

        private View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.btn_item_house_menu:
                    openDialogWindow(modelsList.get(mRecyclerView.getChildAdapterPosition(binding.getRoot())));
                    break;
                default:
                    openVideoActivity(mRecyclerView.getChildAdapterPosition(binding.getRoot()));
                    break;
            }
        };

        public ReviewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().findViewById(R.id.item_up_next_image).setOnClickListener(onClickListener);
            binding.getRoot().findViewById(R.id.item_up_next_address).setOnClickListener(onClickListener);
            binding.getRoot().findViewById(R.id.item_up_next_price).setOnClickListener(onClickListener);
            binding.getRoot().findViewById(R.id.btn_item_house_menu).setOnClickListener(onClickListener);
        }

        private String getTitle() {
            FontTextView textView = (FontTextView) binding.getRoot().findViewById(R.id.item_up_next_address);
            return textView.getText().toString();
        }

        private void openDialogWindow(HouseModel review) {
            DialogFragment fragment = HouseDialogFragment.newInstance(getTitle(), review);
            fragment.show(mContext.getFragmentManager(), "dialog fragment");
        }
    }
}
