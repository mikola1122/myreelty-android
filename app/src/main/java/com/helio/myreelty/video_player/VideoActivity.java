package com.helio.myreelty.video_player;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.EMVideoView;
import com.devbrackets.android.exomedia.listener.EMVideoViewControlsCallback;
import com.helio.myreelty.R;
import com.helio.myreelty.common.User;
import com.helio.myreelty.databinding.ActivityVideoBinding;
import com.helio.myreelty.models.FilesModel;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;
import com.helio.myreelty.navigation.PageFragment;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.user_profile.UserProfileActivity;
import com.helio.myreelty.video_player.util.Share;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Fedir on 23.02.2016.
 */
public class VideoActivity extends AppCompatActivity {

    private HouseModel mHouse;
    private int houseId;
    private EMVideoView emVideoView;
    private static final int FULLSCREEN_REQUEST = 1;
    private ImageView btnBack;
    private ImageView btnShare;
    private ImageView btnLike;
    private ImageView btnBookmark;
    private TabLayout tabLayout;
    private VideoPageAdapter adapter;

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.btn_video_back:
                onBackPressed();
                break;
            case R.id.btn_video_share:
                makeShare();
                break;
            case R.id.btn_video_like:
                makeLike();
                break;
            case R.id.btn_video_bookmark:
                makeBookmark();
                break;
            case R.id.user_profile_info:
                openUserProfileActivity();
            default:
                break;
        }
    };

    private EMVideoViewControlsCallback videoViewControlsCallback = new EMVideoViewControlsCallback() {
        @Override
        public boolean onPlayPauseClicked() {
            return false;
        }

        @Override
        public boolean onFullscreenClicked() {
            startFullscreenActivity();
            return true;
        }

        @Override
        public boolean onPreviousClicked() {
            return false;
        }

        @Override
        public boolean onNextClicked() {
            return false;
        }

        @Override
        public boolean onControlsShown() {
            showToolbarBtns();
            return false;
        }

        @Override
        public boolean onControlsHidden() {
            hideToolbarBtns();
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHouse = HouseSingleton.getInstance().getCurrentHouse();
        houseId = mHouse.id;
        makeFullScreen();
        ActivityVideoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        binding.setHouse(mHouse);
        setupViewPager();
        setupToolbarBtns();
        initVideoPlayer();
        setupVideoPlayer();
        getReview();
    }

    private void openUserProfileActivity() {
        startActivity(new Intent(this, UserProfileActivity.class));
    }

    private void makeLike() {
        if (!mHouse.isLiked()) {
            likeReview();
        } else {
            dislikeReview();
        }
    }

    private void showErrorMessage(Throwable throwable) {
        if (throwable.getMessage().contains("403")) {
            Toast.makeText(this, R.string.please_log_in, Toast.LENGTH_SHORT).show();
        }
    }

    private void makeBookmark() {
        if (!mHouse.isBookmarked()) {
            addToBookmark();
        } else {
            removeFromBookmarks();
        }
    }

    public void addToBookmark() {
        btnBookmark.setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .addToBookmarks(mHouse.getId(), User.getToken(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookmarkModel -> {
                    setBookmarked();
                }, (throwable) -> {
                    showErrorMessage(throwable);
                    btnBookmark.setEnabled(true);
                });
    }

    private void setBookmarked() {
        mHouse.setBookmarked(true);
        changeBookmarkBtnIcon(mHouse.isBookmarked());
        btnBookmark.setEnabled(true);
    }

    public void removeFromBookmarks() {
        btnBookmark.setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .removeFromBookmarks(houseId, User.getToken(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookmarkModel -> {
                    setUnBookmarked();
                }, (throwable) -> {
                    throwable.printStackTrace();
                    btnBookmark.setEnabled(false);
                });
    }

    private void setUnBookmarked() {
        mHouse.setBookmarked(false);
        changeBookmarkBtnIcon(mHouse.isBookmarked());
        btnBookmark.setEnabled(true);
    }

    private void likeReview() {
        btnLike.setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .likeReview(houseId, User.getToken(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setLiked();
                }, (throwable) -> {
                    showErrorMessage(throwable);
                    btnLike.setEnabled(true);
                });
    }

    private void setLiked() {
        mHouse.setLiked(true);
        changeLikeBtnIcon(mHouse.isLiked());
        mHouse.setLikesCount(mHouse.getLikesCount() + 1);
        setLikesCount();
        changeListLikes();
    }

    private void dislikeReview() {
        btnLike.setEnabled(false);
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .dislikeReview(houseId, User.getToken(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    setUnLiked();
                }, (throwable) -> {
                    throwable.printStackTrace();
                    btnLike.setEnabled(true);
                });
    }

    private void setUnLiked() {
        mHouse.setLiked(false);
        changeLikeBtnIcon(mHouse.isLiked());
        mHouse.setLikesCount(mHouse.getLikesCount() - 1);
        setLikesCount();
        changeListLikes();
    }

    private void setLikesCount() {
        tabLayout.getTabAt(VideoPageAdapter.LIKES).setText(mHouse.getLikesCount() + " " + getString(R.string.likes));
    }

    public void setCommentsCount() {
        mHouse.setCommentsCount(mHouse.getCommentsCount() + 1);
        tabLayout.getTabAt(VideoPageAdapter.COMMENTS).setText(mHouse.getCommentsCount() + " " + getString(R.string.comments));
    }

    private void changeListLikes() {
    }

    private void makeShare() {
        Share.shareReview(this, mHouse);
    }

    private void getReview() {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .getReview(houseId, User.getToken(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mHouse = response.getReview();
                    changeLikeBtnIcon(mHouse.isLiked());
                    changeBookmarkBtnIcon(mHouse.isBookmarked());
                }, Throwable::printStackTrace);
    }

    private void changeLikeBtnIcon(boolean isLiked) {
        if (isLiked) {
            btnLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_liked));
        } else {
            btnLike.setImageDrawable(getResources().getDrawable(R.mipmap.ic_like_white));
        }
        btnLike.setEnabled(true);
    }

    private void changeBookmarkBtnIcon(boolean isBookmarked) {
        if (isBookmarked) {
            btnBookmark.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bookmarked));
        } else {
            btnBookmark.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bookmark_border));
        }
        btnBookmark.setEnabled(true);
    }

    private void hideToolbarBtns() {
        btnBack.setVisibility(View.GONE);
        btnLike.setVisibility(View.GONE);
        btnBookmark.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);
    }

    private void showToolbarBtns() {
        btnBack.setVisibility(View.VISIBLE);
        btnLike.setVisibility(View.VISIBLE);
        btnBookmark.setVisibility(View.VISIBLE);
        btnShare.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        emVideoView.pause();
        saveVideoState();
    }

    private void saveVideoState() {
        HouseSingleton.getInstance().setVideoPosition(emVideoView.getCurrentPosition());
        HouseSingleton.getInstance().setPlay(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReview();
        mHouse = HouseSingleton.getInstance().getCurrentHouse();
        emVideoView.setVideoPath(mHouse.getVideoUrl());
        setVideoState((int) HouseSingleton.getInstance().getVideoPosition(), HouseSingleton.getInstance().isPlay());
    }

    @Override
    protected void onPause() {
        super.onPause();
        emVideoView.stopPlayback();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FULLSCREEN_REQUEST) {
            if (resultCode == RESULT_OK) {
                mHouse = getCurrentHouseModel();
                setupVideoPlayer();
            }
        }
    }

    private void setupToolbarBtns() {
        btnBack = (ImageView) findViewById(R.id.btn_video_back);
        btnBack.setOnClickListener(onClickListener);
        btnShare = (ImageView) findViewById(R.id.btn_video_share);
        btnShare.setOnClickListener(onClickListener);
        btnLike = (ImageView) findViewById(R.id.btn_video_like);
        btnLike.setOnClickListener(onClickListener);
        btnBookmark = (ImageView) findViewById(R.id.btn_video_bookmark);
        btnBookmark.setOnClickListener(onClickListener);
        findViewById(R.id.user_profile_info).setOnClickListener(onClickListener);
        findViewById(R.id.btn_video_bookmark).setOnClickListener(onClickListener);
    }

    private void initVideoPlayer() {
        emVideoView = (EMVideoView) findViewById(R.id.video_play_activity_video_view);
        emVideoView.setOnCompletionListener(mp -> emVideoView.setVideoPath(videoLink()));
    }

    private void setupVideoPlayer() {
        try {
            emVideoView.setVideoPath(videoLink());
            emVideoView.setDefaultControlsEnabled(true, false);
            emVideoView.setVideoViewControlsCallback(videoViewControlsCallback);
            setVideoState((int) getCurrentTime(), videoIsPlaying());
        }catch (Exception e){

        }
    }

    private String videoLink() {
        String link = null;
        int square = 0;
        for (FilesModel filesModel : mHouse.getFiles()) {
            if (filesModel.getQuality().equals("sd") && (filesModel.getHeight() * filesModel.getWidth() > square)) {
                square = filesModel.getHeight() * filesModel.getWidth();
                link = filesModel.getLink();
            }
        }
        return link;
    }

    private void setVideoState(int position, boolean isPlay) {
        emVideoView.seekTo(position);
        if (!isPlay) {
            emVideoView.pause();
        } else {
            emVideoView.start();
        }
    }

    private void makeFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private HouseModel getCurrentHouseModel() {
        return HouseSingleton.getInstance().getCurrentHouse();
    }

    private long getCurrentTime() {
        return HouseSingleton.getInstance().getVideoPosition();
    }

    private boolean videoIsPlaying() {
        return HouseSingleton.getInstance().isPlay();
    }

    public HouseModel getHouse() {
        return mHouse;
    }

    private void setupViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.video_viewpager);
        addTabsToViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.video_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addTabsToViewPager(ViewPager viewPager) {
        adapter = new VideoPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new PageFragment(), getString(R.string.up_next));
        adapter.addFragment(new PageFragment(), getString(R.string.details));
        adapter.addFragment(new PageFragment(), mHouse.getCommentsCount() + " \n" + getString(R.string.comments));
        adapter.addFragment(new PageFragment(), mHouse.getLikesCount() + " \n" + getString(R.string.likes));
        viewPager.setAdapter(adapter);
    }

    protected void startFullscreenActivity() {
        HouseSingleton.getInstance().setCurrentHouse(mHouse);
        HouseSingleton.getInstance().setUser(mHouse.getUser());
        HouseSingleton.getInstance().setVideoPosition(emVideoView.getCurrentPosition());
        HouseSingleton.getInstance().setPlay(emVideoView.isPlaying());
        startActivityForResult(new Intent(this, FullScreenVideoActivity.class), FULLSCREEN_REQUEST);
    }
}
