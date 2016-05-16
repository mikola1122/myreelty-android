package com.helio.myreelty.navigation.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.helio.myreelty.R;
import com.helio.myreelty.common.User;
import com.helio.myreelty.common.custom_view.FontButton;
import com.helio.myreelty.common.custom_view.FontEditText;
import com.helio.myreelty.navigation.NavigationActivity;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.NetworkUserModel;
import com.helio.myreelty.network.models.SignInModel;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Taras on 2/25/2016.
 */
public class LogInFragment extends SignUpFragment {

    private View logInView;
    private FontEditText eMail, password;
    private Activity mContext;
    private FontButton btnLogIn;
    private boolean isError;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View logInView = inflater.inflate(R.layout.fragment_log_in, container, false);
        showKeyboard();
        initViews(logInView);
        return logInView;
    }

    private void initViews(View logInView) {
        logInView.findViewById(R.id.link_to_sign_in).setOnClickListener(clickListener);
        btnLogIn = (FontButton)logInView.findViewById(R.id.bt_log_in);
        btnLogIn.setOnClickListener(clickListener);

        eMail = (FontEditText) logInView.findViewById(R.id.log_in_email);
        eMail.setLayerType(eMail.LAYER_TYPE_NONE, null);
        eMail.addTextChangedListener(textWatcher);

        password = (FontEditText) logInView.findViewById(R.id.log_in_password);
        password.addTextChangedListener(textWatcher);
        password.setLayerType(eMail.LAYER_TYPE_HARDWARE, null);

        //TODO need to remove after testing
//        eMail.setText(AppConstant.TEST_EMAIL);
//        password.setText(AppConstant.TEST_PASS);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =(Activity) context;
    }

    private View.OnClickListener clickListener = v -> {
        switch (v.getId()) {
            case R.id.link_to_sign_in:
                openSignUpFragment();
                break;
            case R.id.bt_log_in:
                logIn(v);
                break;
        }
    };

    private void openSignUpFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.log_in_container, new SignUpFragment())
                .commit();
    }

    private void logIn(View v){
        checkInputs();
        logging(eMail.getText().toString(),password.getText().toString());
        hideKeyBoard(v);
    }

    private void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void checkInputs(){
        onErrorEmailInput(eMail, btnLogIn);
        onErrorPasswordInput(password, btnLogIn);
    }

    private void logging(String email, String password){
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .signIn(new SignInModel(email, password))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(logInToken -> {
                    User.setToken(mContext, logInToken.getAuthenticationToken());
                    getUserModel();
                }, this::showErrorMessage);
    }

    private void showErrorMessage(Throwable throwable){
        if(throwable.toString().contains("401")){
            Toast.makeText(mContext, R.string.login_err, Toast.LENGTH_SHORT).show();
        }
        throwable.printStackTrace();
    }

    private void getUserModel() {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .getAccount(User.getToken(mContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    setUser(user);
                    openNavigationActivity();
                }, Throwable::printStackTrace);
    }

    private void setUser(NetworkUserModel user) {
        User.setUser(mContext, user.getAccount());
    }

    private void openNavigationActivity() {
        mContext.startActivity(new Intent(mContext, NavigationActivity.class));
    }

    private boolean isEditComplete() {
        return !(eMail.getText().toString().isEmpty())
                && !(password.getText().toString().isEmpty());
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (isEditComplete()) {
                btnLogIn.setBackground(getResources().getDrawable(R.drawable.ronded_join_with_login));
                btnLogIn.setTextColor(Color.RED);
                btnLogIn.setEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isEditComplete()) {
                btnLogIn.setBackground(getResources().getDrawable(R.drawable.ronded_join_with_login_active));
                btnLogIn.setTextColor(Color.WHITE);
                btnLogIn.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(eMail.isFocused()){
                onErrorEmailInput(eMail, btnLogIn);
            }
            if(password.isFocused()){
                onErrorPasswordInput(password, btnLogIn);
            }
            if (!isEditComplete()) {
                btnLogIn.setEnabled(false);
            }
        }
    };
}