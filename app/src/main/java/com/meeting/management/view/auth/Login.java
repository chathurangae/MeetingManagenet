package com.meeting.management.view.auth;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.meeting.management.R;
import com.meeting.management.api.AuthenticationService;
import com.meeting.management.api.NoNetworkException;
import com.meeting.management.api.RetrofitProvider;
import com.meeting.management.helpers.PreferenceManager;
import com.meeting.management.helpers.ValidationHelper;
import com.meeting.management.model.ApiError;
import com.meeting.management.model.User;
import com.meeting.management.view.BaseActivity;
import com.meeting.management.view.home.MainHome;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class Login extends BaseActivity {

    @BindView(R.id.email_field)
    EditText emailField;
    @BindView(R.id.password_field)
    EditText passwordField;
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;

    private AuthenticationService authService;
    private PreferenceManager manager;
    private Disposable authSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.authService = RetrofitProvider.createRetrofit(this).create(AuthenticationService.class);
        ButterKnife.bind(this);
        manager = new PreferenceManager(this);
        initViews();
    }

    @OnClick(R.id.login_button)
    void onLoginClick() {
        String email = emailField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailField.requestFocus();
            emailField.setError(getString(R.string.error_message_email));
        } else if (!ValidationHelper.isValidEmail(email)) {
            emailField.requestFocus();
            emailField.setError(getString(R.string.error_message_invalid_email));
        } else {
            this.showProgress();

            User userAuthDetails = new User(email);
            userAuth(userAuthDetails);


        }
    }

    private void initViews() {
        if (null != new PreferenceManager(this).getUser()) {
            goToLandingPage();
        }
    }

    private void userAuth(User user) {
        authSubscription = this.makeUIObservable(authService.authenticate(user))
                .subscribe(
                        userInfo -> {
                            this.hideProgress();
                            onSuccess(userInfo.getData().getUser());
                        },
                        error -> {
                            this.hideProgress();
                            if (error instanceof NoNetworkException) {
                                this.noNetworkSnackBar(parentLayout, () -> userAuth(user));
                            } else if (error instanceof ApiError) {
                                ApiError apiError = (ApiError) error;

                                this.showSnackBar(parentLayout,
                                        apiError.getError().get(0).getMessage(),
                                        R.color.red_cinnabar);


                            } else {
                                this.showSnackBar(parentLayout,
                                        error.getMessage(), R.color.red_cinnabar);

                            }
                        }
                );

    }

    private void onSuccess(User response) {
        manager.putUser(response);
        goToLandingPage();
    }

    private void goToLandingPage() {
        goToForwardActivity(MainHome.class);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        if (this.authSubscription != null) {
            this.authSubscription.dispose();
        }
        super.onDestroy();
    }
}
