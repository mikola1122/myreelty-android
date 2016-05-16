package com.helio.myreelty.navigation.profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.helio.myreelty.R;
import com.helio.myreelty.common.custom_view.FontButton;
import com.helio.myreelty.common.custom_view.FontEditText;
import com.helio.myreelty.network.Api;
import com.helio.myreelty.network.RetrofitBuilder;
import com.helio.myreelty.network.models.RegistrationModel;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Taras on 2/25/2016.
 */
public class SignUpFragment extends Fragment{

    private FontEditText etEmail;
    private FontEditText etName;
    private FontEditText etPassword;
    private TextInputLayout textInputLayout;
    private FontButton btnSignUp;
    private View signUpView;
    private int MIN_PASSWORD_LENGTH = 5;
    private int MIN_TEXT_LENGTH = 5;
    private int MAX_TEXT_LENGTH = 30;
    private String EMPTY = "";
    private Context mContext;
    private boolean isError;
    private boolean isInputErr;

    private View.OnClickListener clickListener = v -> {
        switch (v.getId()) {
            case R.id.sign_up_link_to_log_in:
                openLogInFragment();
                break;
            case R.id.bt_sign_up:
                signUp();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        signUpView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        etEmail = (FontEditText) signUpView.findViewById(R.id.sign_up_email);
        etEmail.addTextChangedListener(textWatcher);
        etName = (FontEditText) signUpView.findViewById(R.id.sign_up_name);
        etName.addTextChangedListener(textWatcher);
        etPassword = (FontEditText) signUpView.findViewById(R.id.sign_up_password);
        etPassword.addTextChangedListener(textWatcher);
        signUpView.findViewById(R.id.sign_up_link_to_log_in).setOnClickListener(clickListener);
        btnSignUp = (FontButton) signUpView.findViewById(R.id.bt_sign_up);
        btnSignUp.setOnClickListener(clickListener);
        showKeyboard();
        return signUpView;
    }

    public void checkInputs() {
        onErrorInput(etName);
        onErrorEmailInput(etEmail, btnSignUp);
        onErrorPasswordInput(etPassword, btnSignUp);
    }

    private void signUp() {
        checkInputs();
        if(!isError && !isInputErr) {
            register(etEmail.getText().toString()
                    , etPassword.getText().toString()
                    , etName.getText().toString());
        }
    }

    private void openLogInFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.log_in_container, new LogInFragment())
                .commit();
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void register(String email, String password, String name) {
        RetrofitBuilder
                .createRetrofitService(Api.class)
                .register(new RegistrationModel(email, password, name))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(registrationResponseModel -> {
                    Toast.makeText(mContext,mContext.getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();
                    openLogInFragment();
                }, this::showError);
    }

    private void showError(Throwable throwable) {
        if(throwable.toString().contains("422")){
            Toast.makeText(mContext, R.string.email_err, Toast.LENGTH_SHORT).show();
        }else {
            throwable.printStackTrace();
            Toast.makeText(mContext,mContext.getString(R.string.registration_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void onErrorPasswordInput(FontEditText text, FontButton fontButton) {
        textInputLayout = null;
        textInputLayout = (TextInputLayout) text.getParent();
        if (text.getText().length() < MIN_PASSWORD_LENGTH) {
            textInputLayout.setError(getResources().getString(R.string.error_message_short_password));
            textInputLayout.setErrorEnabled(true);
            isError = true;
            fontButton.setEnabled(false);
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            isError = false;
            fontButton.setEnabled(true);
        }
    }

    public final boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void onErrorInput(FontEditText text) {
        textInputLayout = (TextInputLayout) text.getParent();
        if (text.getText().length() < MIN_TEXT_LENGTH  || text.getText().length() > MAX_TEXT_LENGTH) {
            textInputLayout.setError(getResources().getString(R.string.error_message_user_name));
            textInputLayout.setErrorEnabled(true);
            isInputErr = true;
            btnSignUp.setEnabled(false);
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            isInputErr = false;
            btnSignUp.setEnabled(true);
        }
    }

    public void onErrorEmailInput(FontEditText text, FontButton fontButton) {
        textInputLayout = (TextInputLayout) text.getParent();
        if (!isValidEmail(text.getText())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getResources().getString(R.string.error_message_email));
            isError = true;
            fontButton.setEnabled(false);
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            isError = false;
            fontButton.setEnabled(true);
        }
    }

    private boolean isEditComplete() {
        return (!etEmail.getText().toString().equals(EMPTY)
                & !etPassword.getText().toString().equals(EMPTY)
                & !etName.getText().toString().equals(EMPTY));
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!isEditComplete()) {
                btnSignUp.setBackground(getResources().getDrawable(R.drawable.ronded_join_with_login));
                btnSignUp.setTextColor(Color.RED);
                btnSignUp.setEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isEditComplete()) {
                btnSignUp.setBackground(getResources().getDrawable(R.drawable.ronded_join_with_login_active));
                btnSignUp.setTextColor(Color.WHITE);
                btnSignUp.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if(etName.isFocused()){
                onErrorInput(etName);
            }
            if(etEmail.isFocused()){
                onErrorEmailInput(etEmail, btnSignUp);
            }
            if(etPassword.isFocused()){
                onErrorPasswordInput(etPassword, btnSignUp);
            }
            if (!isEditComplete()) {
                btnSignUp.setEnabled(false);
            }else btnSignUp.setEnabled(true);
        }
    };
}
