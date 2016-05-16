package com.helio.myreelty.video_player.comments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.helio.myreelty.R;
import com.helio.myreelty.common.User;
import com.helio.myreelty.common.pagination.PaginationRecyclerAdapter;
import com.helio.myreelty.databinding.ItemVideoCommentsBinding;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.CommentContentModel;
import com.helio.myreelty.network.models.NetworkCommentModel;
import com.helio.myreelty.network.models.VideoAddCommentModel;
import com.helio.myreelty.network.models.VideoCommentResponseModel;
import com.helio.myreelty.network.models.VideoCommentsModel;
import com.helio.myreelty.user_profile.UserProfileActivity;
import com.helio.myreelty.video_player.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nicolas on 04.03.2016.
 */
public class VideoCommentsAdapter extends PaginationRecyclerAdapter {
    public static final int TYPE_ITEM_ADD_COMMENT = 0;
    public static final int TYPE_ITEM_COMMENTS = 1;
    public static final int MIN_COMMENT_LENGTH = 2;
    public static final int FIRST_POSITION_OF_LIST = 1;

    private List<Object> mCommentsList = new ArrayList<>();
    private String mToken;
    private Context mContext;


    public VideoCommentsAdapter(Context context) {
        mToken = User.getToken(context);
        this.mContext = context;
        mCommentsList.add(new AddCommentModel());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case TYPE_ITEM_ADD_COMMENT:
                AddVideoCommentHolder addVideoCommentHolder = new AddVideoCommentHolder(inflater.inflate(R.layout.item_add_video_comment, parent, false));

                addVideoCommentHolder.binding.addVideoCommentSendIcon.setOnClickListener(v -> addCommentsClickListener(addVideoCommentHolder));
                addVideoCommentHolder.binding.addVideoCommentContent.setOnEditorActionListener((v, actionId, event) ->
                        buttonSendPressCheck((EditText) v, actionId));
                return addVideoCommentHolder;
            default:
                return new VideoCommentHolder(DataBindingUtil.inflate(inflater, R.layout.item_video_comments,
                        parent, false).getRoot());
        }
    }

    private void addCommentsClickListener(AddVideoCommentHolder addVideoCommentHolder) {
        if (!mToken.isEmpty()) {
            sendNewCommentToServer(addVideoCommentHolder.binding.addVideoCommentContent);
        } else {
            showToastAboutCommentStatus(R.string.please_log_in);
        }
    }

    private void sendNewCommentToServer(EditText commentTxt) {
        if (commentTxt.getText().toString().length() > MIN_COMMENT_LENGTH) {
            sendCommentRequest(commentTxt);
        } else {
            showToastAboutCommentStatus(R.string.too_short_comment);
        }
    }

    private void sendCommentRequest(EditText commentTxt) {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .addComment(((VideoActivity) mContext).getHouse().getId(), mToken,
                        new VideoAddCommentModel(new CommentContentModel(commentTxt.getText().toString())))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoAddCommentResponseModel -> {
                    addCommentWasSuccessful(commentTxt, videoAddCommentResponseModel);
                }, Throwable::printStackTrace);
    }


    private void addCommentWasSuccessful(EditText commentTxt, VideoCommentResponseModel videoAddCommentResponseModel) {
        hideSoftKeyboard();
        commentTxt.setText("");
        commentTxt.clearFocus();
        showToastAboutCommentStatus(R.string.comment_added);
        addNewCommentToList(videoAddCommentResponseModel);
        ((VideoActivity) mContext).setCommentsCount();
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(((VideoActivity) mContext)
                .getCurrentFocus().getWindowToken(), 0);
    }

    private void showToastAboutCommentStatus(@StringRes int status) {
        Toast shortComment = Toast.makeText(mContext, status, Toast.LENGTH_SHORT);
        shortComment.setGravity(Gravity.CENTER, 0, 0);
        shortComment.show();
    }

    private void addNewCommentToList(VideoCommentResponseModel commentModel) {
        mCommentsList.add(FIRST_POSITION_OF_LIST, commentModel.getNetworkCommentModels());
        notifyItemInserted(FIRST_POSITION_OF_LIST);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM_ADD_COMMENT:
                break;
            case TYPE_ITEM_COMMENTS:
                VideoCommentHolder commentHolder = (VideoCommentHolder) holder;
                NetworkCommentModel commentsModel = (NetworkCommentModel) mCommentsList.get(position);
                commentHolder.binding.setComments(commentsModel);
                break;
        }

    }

    private boolean buttonSendPressCheck(EditText v, int actionId) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            if (!mToken.isEmpty()) {
                sendNewCommentToServer(v);
            } else {
                showToastAboutCommentStatus(R.string.please_log_in);
            }
            handled = true;
        }
        return handled;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCommentsList.get(position) instanceof AddCommentModel)
            return TYPE_ITEM_ADD_COMMENT;
        else
            return TYPE_ITEM_COMMENTS;
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    public void addComments(VideoCommentsModel model) {
        mCommentsList.addAll(model.getCommentsModels());
        notifyDataSetChanged();
    }

    public class VideoCommentHolder extends RecyclerView.ViewHolder {

        private ItemVideoCommentsBinding binding;

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseSingleton.getInstance().setUser(binding.getComments().getUser());
                mContext.startActivity(new Intent(mContext, UserProfileActivity.class));
            }
        };

        public VideoCommentHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().setOnClickListener(onClickListener);
        }
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso
                .with(imageView.getContext())
                .load(url)
                .resize(50,50)
                .centerCrop()
                .onlyScaleDown()
                .into(imageView);
    }
}

