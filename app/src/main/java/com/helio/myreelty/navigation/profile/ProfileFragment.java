package com.helio.myreelty.navigation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helio.myreelty.R;

/**
 * Created by Fedir on 25.02.2016.
 */
public class ProfileFragment extends Fragment {

    private final static int LOG_IN_FRAGMENT = 0;
    private final static int SIGN_UP_FRAGMENT = 1;
    private final static String BUNDLE_TAG = "fragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_myreelty, container, false);
        view.findViewById(R.id.bt_profile_sign_up).setOnClickListener(onClickListener);
        view.findViewById(R.id.bt_join_with_facebook).setOnClickListener(onClickListener);
        view.findViewById(R.id.profile_link_to_log_in).setOnClickListener(onClickListener);
        return view;
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.bt_profile_sign_up:
                openSignUpFragment();
                break;
            case R.id.bt_join_with_facebook:

                break;
            case R.id.profile_link_to_log_in:
                openLogInFragment();
                break;
        }
    };

    private void openLogInFragment() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LogInActivity.class);
        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putInt(BUNDLE_TAG,LOG_IN_FRAGMENT);
        intent.putExtras(savedInstanceState);
        startActivity(intent);
    }

    private void openSignUpFragment() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LogInActivity.class);
        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putInt(BUNDLE_TAG,SIGN_UP_FRAGMENT);
        intent.putExtras(savedInstanceState);
        startActivity(intent);
    }
}
