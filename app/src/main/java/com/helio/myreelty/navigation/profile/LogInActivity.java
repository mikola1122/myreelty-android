package com.helio.myreelty.navigation.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.helio.myreelty.R;

/**
 * Created by Taras on 2/25/2016.
 */
public class LogInActivity extends AppCompatActivity {

    private final static int SIGN_UP_FRAGMENT = 1;
    private final static String BUNDLE_TAG = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setupToolBar();
        openSelectedFragment();
    }

    private void openSelectedFragment() {
        Bundle extras = getIntent().getExtras();
        if (extras.getInt(BUNDLE_TAG) == SIGN_UP_FRAGMENT) {
            openSignUpFragment();
        } else {
            openLogInFragment();
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.log_in__activity_toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.log_in__activity_toolbar).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = v -> {
        hideKeyboard();
        onBackPressed();
    };

    private void openSignUpFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.log_in_container, new SignUpFragment())
                .commit();
    }

    private void openLogInFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.log_in_container, new LogInFragment())
                .commit();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}
