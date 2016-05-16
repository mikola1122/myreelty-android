package com.helio.myreelty.video_player.likes;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.helio.myreelty.R;
import com.helio.myreelty.common.pagination.PaginationRecyclerAdapter;
import com.helio.myreelty.databinding.ItemLikeFragmentBinding;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.network.models.LikeModel;
import com.helio.myreelty.user_profile.UserProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taras on 31.03.2016.
 */
public class LikesAdapter extends PaginationRecyclerAdapter {

    private ArrayList<LikeModel> mLikesList;
    private Activity mContext;
    private LikeModel likeModel;

    public LikesAdapter(ArrayList<LikeModel> mLikesList, Activity context) {
        this.mLikesList = mLikesList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LikeViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_like_fragment, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LikeViewHolder likeHolder = (LikeViewHolder) holder;
        likeModel = mLikesList.get(position);
        likeHolder.binding.setLike(likeModel);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
            return mLikesList.size();
    }

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso
                .with(imageView.getContext())
                .load(url)
                .resize(50,50)
                .onlyScaleDown()
                .centerCrop()
                .placeholder(R.mipmap.test_profile_picture)
                .into(imageView);
    }

    public void addItems(List<LikeModel> likes) {
        mLikesList.addAll(likes);
        notifyDataSetChanged();

    }

    class LikeViewHolder extends RecyclerView.ViewHolder {

        ItemLikeFragmentBinding binding;

        private View.OnClickListener onClickListener = v -> openVideoActivity();

        private void openVideoActivity(){
            HouseSingleton.getInstance().setUser(binding.getLike().getUser());
            mContext.startActivity(new Intent(mContext, UserProfileActivity.class));
        }

        public LikeViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().setOnClickListener(onClickListener);

        }
    }
}
