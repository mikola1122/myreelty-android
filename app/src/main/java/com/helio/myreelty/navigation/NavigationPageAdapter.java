package com.helio.myreelty.navigation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.helio.myreelty.common.User;
import com.helio.myreelty.navigation.bookmark.BookmarkFragment;
import com.helio.myreelty.navigation.home.HomeFragment;
import com.helio.myreelty.navigation.profile.MyProfileFragment;
import com.helio.myreelty.navigation.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markus on 04.02.2016.
 */
public class NavigationPageAdapter extends FragmentPagerAdapter {

    private static final int HOME_PAGE = 0;
    private static final int BOOKMARKS_PAGE = 1;
    private static final int PROFILE_PAGE = 2;
    private static Context mContext;

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public static NavigationPageAdapter newInstance(Context context, FragmentManager fragmentManager) {
        mContext = context;
        return  new NavigationPageAdapter(fragmentManager);
    }

    public NavigationPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case HOME_PAGE:
                return new HomeFragment();
            case PROFILE_PAGE:
               return getProfileFragment();
            case BOOKMARKS_PAGE:
                return new BookmarkFragment();
        }
        return mFragmentList.get(position);
    }



    private Fragment getProfileFragment() {
        if(User.isLogedIn(mContext)){
            return new MyProfileFragment();
        }
        return new ProfileFragment();
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

}