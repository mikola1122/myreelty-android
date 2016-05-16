package com.helio.myreelty.video_player;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.helio.myreelty.R;
import com.helio.myreelty.video_player.comments.VideoCommentsFragment;
import com.helio.myreelty.video_player.likes.LikesFragment;
import com.helio.myreelty.video_player.video_details.VideoDetailsFragment;
import com.helio.myreelty.video_player.video_up_next.VideoUpNextFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fedir on 23.02.2016.
 */
public class VideoPageAdapter extends FragmentPagerAdapter {

    public static final int UP_NEXT = 0;
    public static final int DETAILS = 1;
    public static final int COMMENTS = 2;
    public static final int LIKES = 3;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public VideoPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case UP_NEXT:
                return new VideoUpNextFragment();
            case DETAILS:
                return new VideoDetailsFragment();
            case COMMENTS:
                return new VideoCommentsFragment();
            case LIKES:
                return new LikesFragment();
        }

        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}