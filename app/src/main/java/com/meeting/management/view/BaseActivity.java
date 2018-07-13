package com.meeting.management.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.meeting.management.R;
import com.meeting.management.api.RetrofitProvider;
import com.meeting.management.helpers.CustomProgressDialog;

import io.reactivex.Observable;

public class BaseActivity extends AppCompatActivity {

    private CustomProgressDialog progressDialog;
    private SnackBarAction snackBarAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        this.progressDialog = new CustomProgressDialog(this);

    }

    public void goToForwardActivity(Class<? extends Activity> activity) {
        launchActivity(activity);
        this.overridePendingTransition(R.anim.forward_in, R.anim.forward_out);
    }

    public void goToBackActivity(Class<? extends Activity> activity) {
        launchActivity(activity);
        this.overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    public void launchActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void showProgress() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public <T> Observable<T> makeUIObservable(Observable<T> observable) {
        return RetrofitProvider.configureObservable(observable);
    }

    protected void noNetworkSnackBar(View mainLayout,
                                     SnackBarAction snackBarAction) {
        this.snackBarAction = snackBarAction;
        Snackbar snackbar = Snackbar
                .make(mainLayout, R.string.text_no_network, Snackbar.LENGTH_LONG)
                .setDuration(8000)
                .setAction("RETRY", view -> this.snackBarAction.retry());
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.red_cinnabar));
        snackbar.show();
    }

    protected void showSnackBar(View mainLayout, String message, int backgroundColour) {
        Snackbar snackbar = Snackbar
                .make(mainLayout, message, Snackbar.LENGTH_LONG).setDuration(5000);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, backgroundColour));
        snackbar.show();
    }


    public interface SnackBarAction {
        void retry();
    }


}
