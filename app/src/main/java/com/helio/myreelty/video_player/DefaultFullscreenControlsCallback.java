package com.helio.myreelty.video_player;

import com.devbrackets.android.exomedia.listener.EMVideoViewControlsCallback;

/**
 * Created by Nicolas on 17.03.2016.
 */
class DefaultFullscreenControlsCallback implements EMVideoViewControlsCallback {
    private FullScreenVideoActivity fullScreenVideoActivity;

    public DefaultFullscreenControlsCallback(FullScreenVideoActivity fullScreenVideoActivity) {
        this.fullScreenVideoActivity = fullScreenVideoActivity;
    }

    @Override
    public boolean onPlayPauseClicked() {
        return false;
    }

    @Override
    public boolean onFullscreenClicked() {
        fullScreenVideoActivity.backToVideoActivity();
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

        return false;
    }

    @Override
    public boolean onControlsHidden() {
        fullScreenVideoActivity.hideNavigationPanel();
        return false;
    }
}
