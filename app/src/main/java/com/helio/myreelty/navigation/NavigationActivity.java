package com.helio.myreelty.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.helio.myreelty.R;
import com.helio.myreelty.common.custom_view.FontTextView;
import com.helio.myreelty.search.SearchActivity;

/**
 * Created by markus on 04.02.2016.
 */
public class NavigationActivity extends AppCompatActivity {

    private static final int HOME = 0;
    private static final int BOOKMARKS = 1;
    private static final int PROFILE = 2;
    private ViewPager viewPager;

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            changeTabToActiveIcon(tab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            changeTabToInactiveIcon(tab);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            changeTabToActiveIcon(tab);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setupToolBar();
        setupViewPager();
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addTabsToViewPager(viewPager);
        setupTabLayout(viewPager);
    }

    private void setupTabLayout(ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        setupTabIcon(tabLayout);
        tabLayout.setOnTabSelectedListener(tabSelectedListener);
    }

    private void setupTabIcon(TabLayout tabLayout) {
        setHomeIcon(tabLayout);
        setBookmarksIcon(tabLayout);
        setProfileIcon(tabLayout);
    }

    private void setProfileIcon(TabLayout tabLayout) {
        TabLayout.Tab tab = tabLayout.getTabAt(PROFILE);
        if (tab != null)
            tab.setIcon(R.mipmap.ic_people_black_24_px);
    }

    private void setBookmarksIcon(TabLayout tabLayout) {
        TabLayout.Tab tab = tabLayout.getTabAt(BOOKMARKS);
        if (tab != null)
            tab.setIcon(R.drawable.ic_bookmark_gray_24_px);
    }

    private void setHomeIcon(TabLayout tabLayout) {
        TabLayout.Tab tab = tabLayout.getTabAt(HOME);
        if (tab != null)
            tab.setIcon(R.mipmap.ic_home_white_24_px);
    }

    private void changeToolbarTitle(int textId) {
        FontTextView toolbarName = (FontTextView) findViewById(R.id.toolbar_name);
        toolbarName.setText(textId);
    }

    private void changeTabToActiveIcon(TabLayout.Tab tab) {
        int resId;
        switch (tab.getPosition()) {
            case HOME:
                resId = R.mipmap.ic_home_white_24_px;
                viewPager.setCurrentItem(HOME);
                changeToolbarTitle(R.string.app_name);
                break;
            case BOOKMARKS:
                resId = R.drawable.ic_bookmark_white_24_px;
                viewPager.setCurrentItem(BOOKMARKS);
                changeToolbarTitle(R.string.bookmarks);
                break;
            case PROFILE:
                resId = R.mipmap.ic_profile;
                viewPager.setCurrentItem(PROFILE);
                changeToolbarTitle(R.string.profile);
                break;
            default:
                resId = 0;
                break;
        }

        tab.setIcon(resId);
    }

    private void changeTabToInactiveIcon(TabLayout.Tab tab) {
        int resId;
        switch (tab.getPosition()) {
            case HOME:
                resId = R.mipmap.ic_home_black_24_px;
                break;
            case BOOKMARKS:
                resId = R.mipmap.ic_bookmark_gray_24_px;
                break;
            case PROFILE:
                resId = R.mipmap.ic_people_black_24_px;
                break;
            default:
                resId = 0;
                break;
        }
        tab.setIcon(resId);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.navigation_search).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = v -> openSearchActivity();


    // TODO set list of reviews to bundle
    private void openSearchActivity() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    private void addTabsToViewPager(ViewPager viewPager) {
        NavigationPageAdapter adapter = NavigationPageAdapter.newInstance(this, getSupportFragmentManager());
        adapter.addFragment(new PageFragment());
        adapter.addFragment(new PageFragment());
        adapter.addFragment(new PageFragment());
        viewPager.setAdapter(adapter);
    }
}
