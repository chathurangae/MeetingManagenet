package com.meeting.management.view;


import android.os.Bundle;

import com.meeting.management.R;
import com.meeting.management.view.auth.Login;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class Splash extends BaseActivity {

    private static final long SPLASH_TIME = 2L;
    private Disposable timerSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.timerSubscription = this.makeUIObservable(
                Observable.timer(SPLASH_TIME, TimeUnit.SECONDS)
        ).subscribe(time -> {
            this.goToForwardActivity(Login.class);
            this.finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerSubscription != null) {
            timerSubscription.dispose();
        }
    }
}
