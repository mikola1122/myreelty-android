package com.helio.myreelty.user_profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.helio.myreelty.R;

/**
 * Created by Taras on 05.04.2016.
 */
public class UserProfileActivity extends AppCompatActivity {

    private View.OnClickListener onClickListener = v -> onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        findViewById(R.id.user_profile_back_button).setOnClickListener(onClickListener);
    }
}
