package com.helio.myreelty.video_player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.devbrackets.android.exomedia.EMVideoView;
import com.helio.myreelty.R;
import com.helio.myreelty.models.FilesModel;
import com.helio.myreelty.models.HouseModel;
import com.helio.myreelty.models.HouseSingleton;

/**
 * Created by Nicolas on 14.03.2016.
 */
public class FullScreenVideoActivity extends Activity {

    private FullScreenListener fullScreenListener;
    private HouseModel mHouse;
    private EMVideoView emVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHouse = HouseSingleton.getInstance().getCurrentHouse();
        makeFullScreen();
        setContentView(R.layout.activity_full_screen_video_player);
        setupVideoPlayer();
        hideNavigationPanel();
    }

    private void setupVideoPlayer() {
        emVideoView = (EMVideoView) findViewById(R.id.full_screen_video_play_activity_video_view);
        emVideoView.setVideoPath(videoLink());
        emVideoView.setDefaultControlsEnabled(true, true);
        emVideoView.setOnCompletionListener(mp -> emVideoView.setVideoPath(videoLink()));


        setVideoState((int) getVideoPosition(), isPlay());

        fullScreenListener = new FullScreenListener();
        emVideoView.setVideoViewControlsCallback(new DefaultFullscreenControlsCallback(this));
    }

    private void setVideoState(int position, boolean isPlay) {
        emVideoView.seekTo(position);
        if (!isPlay) {
            emVideoView.pause();
        } else {
            emVideoView.start();
        }
    }

    private String videoLink() {
        String link = null;
        int square = 0;
        for (FilesModel filesModel : mHouse.getFiles()) {
            if (filesModel.getQuality().equals("hd") && (filesModel.getHeight() * filesModel.getWidth() > square)) {
                square = filesModel.getHeight() * filesModel.getWidth();
                link = filesModel.getLink();
            }
        }
        return link;
    }

    private long getVideoPosition() {
        return HouseSingleton.getInstance().getVideoPosition();
    }

    private boolean isPlay() {
        return HouseSingleton.getInstance().isPlay();
    }

    private void makeFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        emVideoView.pause();
        saveVideoState();
    }

    private void saveVideoState() {
        HouseSingleton.getInstance().setVideoPosition(emVideoView.getCurrentPosition());
        HouseSingleton.getInstance().setPlay(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        emVideoView.setVideoPath(videoLink());
        setVideoState((int) HouseSingleton.getInstance().getVideoPosition(), HouseSingleton.getInstance().isPlay());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showNavigationPanel();
    }

    @Override
    public void onBackPressed() {
        backToVideoActivity();
    }

    protected void backToVideoActivity() {
        HouseSingleton.getInstance().setCurrentHouse(mHouse);
        HouseSingleton.getInstance().setVideoPosition(emVideoView.getCurrentPosition());
        HouseSingleton.getInstance().setPlay(emVideoView.isPlaying());
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        emVideoView.pause();
        finish();
    }

    protected void hideNavigationPanel() {
        setUiFlags(true);
    }

    private void showNavigationPanel() {
        setUiFlags(false);
    }

    private void setUiFlags(boolean fullscreen) {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(fullscreen ? getFullscreenUiFlags() : View.SYSTEM_UI_FLAG_VISIBLE);
            decorView.setOnSystemUiVisibilityChangeListener(fullScreenListener);
        }
    }

    private int getFullscreenUiFlags() {
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        return flags;
    }

    private class FullScreenListener implements View.OnSystemUiVisibilityChangeListener {
        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                emVideoView.showDefaultControls();
            }
        }
    }
}
