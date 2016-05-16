package com.helio.myreelty.common.dialog;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.helio.myreelty.R;
import com.helio.myreelty.common.User;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.databinding.ItemHouseDialogBinding;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.video_player.util.Share;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Taras on 3/1/2016.
 */
public class DialogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemDialogModel> mItemDialogList;
    private final static int LIKE = 0;
    private final static int BOOKMARK = 1;
    private final static int SHARE = 2;
    private HouseModel mHouseModel = HouseSingleton.getInstance().getCurrentHouse();
    private Context mContext;
    private String mToken;

    public DialogListAdapter(Context context, HouseModel house) {
        mContext = context;
        if(house != null) {
            mHouseModel = house;
        }
        mToken = User.getToken(context);
        initItemDialogList(context);

    }

    private void initItemDialogList(Context context){
        mItemDialogList = new ArrayList<>();
        mItemDialogList.add(getLikeDialogModel(context));
        mItemDialogList.add(getBookmarkDialogModel(context));
        mItemDialogList.add(new ItemDialogModel(context.getResources().getString(R.string.share_this_video), R.mipmap.ic_share));
    }

    private ItemDialogModel getLikeDialogModel(Context context) {
        if (mHouseModel.isLiked()) {
            return new ItemDialogModel(context.getResources().getString(R.string.liked), R.mipmap.ic_liked);
        } else {
            return new ItemDialogModel(context.getResources().getString(R.string.like), R.mipmap.ic_like);
        }
    }

    private ItemDialogModel getBookmarkDialogModel(Context context) {
        if (mHouseModel.isBookmarked()) {
            return new ItemDialogModel(context.getResources().getString(R.string.remove_from_bookmarks), R.mipmap.ic_bookmark_primary_24_px);
        } else {
            return new ItemDialogModel(context.getResources().getString(R.string.add_to_bookmarks), R.mipmap.ic_bookmark_border_white_24_px);
        }
    }

    public DialogListAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new DialogItemViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_house_dialog, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DialogItemViewHolder dialogHolder = (DialogItemViewHolder) holder;
        ItemDialogModel houseModel = mItemDialogList.get(position);
        dialogHolder.binding.setDialogModel(houseModel);
        setTextColor(dialogHolder, position);

        dialogHolder.binding.getRoot().setOnClickListener(v -> {
            switch (position) {
                case LIKE:
                    changeLikeItem(mItemDialogList.get(position)
                            , dialogHolder
                            , R.mipmap.ic_liked
                            , R.string.liked
                            , R.mipmap.ic_like
                            , R.string.like);
                    break;
                case BOOKMARK:
                    changeBookmarkItem(mItemDialogList.get(position)
                            , dialogHolder
                            , R.mipmap.ic_bookmark_primary_24_px
                            , R.string.remove_from_bookmarks
                            , R.mipmap.ic_bookmark_border_white_24_px
                            , R.string.add_to_bookmarks);
                    break;
                case SHARE:
                    Share.shareReview(mContext, mHouseModel);
                    break;
            }
        });
    }

    private void setTextColor(DialogItemViewHolder holder, int position){
        if (position == LIKE && mHouseModel.isLiked()) {
            setTextColorActive(holder);
        } else if (mHouseModel.isBookmarked() && position == BOOKMARK) {
            setTextColorActive(holder);
        } else {
            setTextColorInActive(holder);
        }
    }

    private void setTextColorActive(DialogItemViewHolder holder){
        FontTextView textView = (FontTextView) holder.binding.getRoot().findViewById(R.id.item_dialog_text);
        textView.setTextColor(mContext.getResources().getColor(R.color.coral_pink));
    }

    private void setTextColorInActive(DialogItemViewHolder holder){
        FontTextView textView = (FontTextView) holder.binding.getRoot().findViewById(R.id.item_dialog_text);
        textView.setTextColor(mContext.getResources().getColor(R.color.warm_grey));
    }

    public void changeBookmarkItem(ItemDialogModel houseModel
            , DialogItemViewHolder dialogHolder
            , int activeImgId
            , int activeStrId
            , int inactiveImgId
            , int inactiveStrId) {

        if (!mHouseModel.isBookmarked()) {
            addToBookmark(dialogHolder, houseModel, activeImgId, activeStrId);
        } else {
            removeFromBookmarks(dialogHolder, houseModel, inactiveImgId, inactiveStrId);
        }
    }

    public void changeLikeItem(ItemDialogModel houseModel
            , DialogItemViewHolder dialogHolder
            , int activeImgId
            , int activeStrId
            , int inactiveImgId
            , int inactiveStrId) {

        if (!mHouseModel.isLiked()) {
            likeReview(dialogHolder, houseModel, activeImgId, activeStrId);
        } else {
            dislikeReview(dialogHolder, houseModel, inactiveImgId, inactiveStrId);
        }
    }

    private void showErrorMessage(Throwable throwable, DialogItemViewHolder dialogHolder){
        if (throwable.getMessage().contains(mContext.getResources().getString(R.string.forbidden_error))){
            Toast.makeText(mContext, mContext.getResources().getString(R.string.please_log_in), Toast.LENGTH_SHORT).show();
            dialogHolder.binding.getRoot().setEnabled(true);
        }
    }

    public void addToBookmark(DialogItemViewHolder dialogHolder, ItemDialogModel bookmarkModel, int imageId, int stringId) {
        dialogHolder.binding.getRoot().setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .addToBookmarks(mHouseModel.getId(), mToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setItemBookmarked(dialogHolder, bookmarkModel, imageId, stringId);
                }, throwable -> {
                    showErrorMessage(throwable, dialogHolder);
                });
    }

    private void setItemBookmarked(DialogItemViewHolder dialogHolder, ItemDialogModel bookmarkModel, int imageId, int stringId){
        changeToActive(dialogHolder, bookmarkModel, imageId, stringId);
        mHouseModel.setBookmarked(true);
        notifyItemChanged(BOOKMARK);
        dialogHolder.binding.getRoot().setEnabled(true);
    }

    public void removeFromBookmarks(DialogItemViewHolder dialogHolder, ItemDialogModel bookmarkModel, int imageId, int stringId) {
        dialogHolder.binding.getRoot().setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .removeFromBookmarks(mHouseModel.getId(), mToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setItemUnBookmarked(dialogHolder, bookmarkModel, imageId, stringId);
                }, (throwable) -> {
                    dialogHolder.binding.getRoot().setEnabled(true);
                });
    }

    private void setItemUnBookmarked(DialogItemViewHolder dialogHolder, ItemDialogModel bookmarkModel, int imageId, int stringId){
        changeToInactive(dialogHolder, bookmarkModel, imageId, stringId);
        mHouseModel.setBookmarked(false);
        notifyItemChanged(BOOKMARK);
        dialogHolder.binding.getRoot().setEnabled(true);
    }

    private void likeReview(DialogItemViewHolder dialogHolder, ItemDialogModel likeModel, int activeImgId, int activeStrId) {
        dialogHolder.binding.getRoot().setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .likeReview(mHouseModel.getId(), mToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setItemLiked(dialogHolder, likeModel, activeImgId, activeStrId);
                }, (throwable) -> {
                    showErrorMessage(throwable,dialogHolder);
                });
    }

    private void setItemLiked(DialogItemViewHolder dialogHolder, ItemDialogModel likeModel, int activeImgId, int activeStrId){
        changeToActive(dialogHolder, likeModel, activeImgId, activeStrId);
        mHouseModel.setLiked(true);
        dialogHolder.binding.getRoot().setEnabled(true);
        notifyItemChanged(LIKE);
    }

    private void dislikeReview(DialogItemViewHolder dialogHolder, ItemDialogModel likeModel, int inactiveImgId, int inactiveStrId) {
        dialogHolder.binding.getRoot().setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .dislikeReview(mHouseModel.getId(), mToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setItemDisliked(dialogHolder, likeModel, inactiveImgId, inactiveStrId);
                }, (throwable) -> {
                    dialogHolder.binding.getRoot().setEnabled(true);
                });
    }

    private void setItemDisliked(DialogItemViewHolder dialogHolder, ItemDialogModel likeModel, int inactiveImgId, int inActiveStrId){
        changeToInactive(dialogHolder, likeModel, inactiveImgId, inActiveStrId);
        mHouseModel.setLiked(false);
        notifyItemChanged(LIKE);
        dialogHolder.binding.getRoot().setEnabled(true);
    }

    public void changeToActive(DialogItemViewHolder dialogHolder, ItemDialogModel houseModel, int imageId, int stringId) {
        houseModel.setItemName(mContext.getString(stringId));
        houseModel.setResource(imageId);
        setTextColorInActive(dialogHolder);
    }

    public void changeToInactive(DialogItemViewHolder dialogHolder, ItemDialogModel houseModel, int imageId, int stringId) {
        houseModel.setResource(imageId);
        houseModel.setItemName(mContext.getString(stringId));
        setTextColorActive(dialogHolder);
    }

    @Override
    public int getItemCount() {
        return mItemDialogList.size();
    }

    @BindingAdapter("app:loadImage")
    public static void setImageId(ImageView view, int resId) {
        Picasso.with(view.getContext()).load(resId).into(view);
    }

    public class DialogItemViewHolder extends RecyclerView.ViewHolder {
        public ItemHouseDialogBinding binding;

        public DialogItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
