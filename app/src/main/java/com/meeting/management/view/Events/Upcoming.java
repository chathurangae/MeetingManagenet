package com.meeting.management.view.Events;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meeting.management.R;
import com.meeting.management.api.MeetingService;
import com.meeting.management.api.NoNetworkException;
import com.meeting.management.api.RetrofitProvider;
import com.meeting.management.helpers.PreferenceManager;
import com.meeting.management.model.ApiError;
import com.meeting.management.model.Meetings;
import com.meeting.management.view.home.MainHome;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


public class Upcoming extends Fragment {

    private PreferenceManager manager;
    private MainHome mainHome;
    private String userId;
    private Disposable meetingSubscription;
    private MeetingService meetingService;
    private List<Meetings> meetingsList;

    @BindView(R.id.events_recycler_view)
    RecyclerView eventsView;


    public Upcoming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        this.meetingService = RetrofitProvider.createRetrofit(getContext()).create(MeetingService.class);

        Activity activity = getActivity();
        if (activity instanceof MainHome) {
            mainHome = (MainHome) activity;
        }
        ButterKnife.bind(mainHome);
        manager = new PreferenceManager(getContext());

        userId = manager.getUser().getUserId();

        getMeetingsInfo();
        return view;
    }

    private void getMeetingsInfo() {
        mainHome.showProgress();
        meetingSubscription = mainHome.makeUIObservable(meetingService.getmeetingInfo(userId))
                .subscribe(
                        meeting -> {
                            meetingsList = new ArrayList<>();
                            meetingsList.addAll(meeting.getData().getMeetings());

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mainHome.getApplicationContext());
                            eventsView.setLayoutManager(mLayoutManager);
                            eventsView.setItemAnimator(new DefaultItemAnimator());

                            UpcomingAdapter mAdapter = new UpcomingAdapter(meetingsList);
                            eventsView.setAdapter(mAdapter);

                            mainHome.hideProgress();


                        },
                        error -> {
                            mainHome.hideProgress();
                            if (error instanceof NoNetworkException) {
                                mainHome.noNetworkSnackBar(this::getMeetingsInfo);
                            } else if (error instanceof ApiError) {
                                ApiError apiError = (ApiError) error;

                                mainHome.showSnackBar(apiError.getError().get(0).getMessage()
                                        , R.color.red_cinnabar);


                            } else {
                                mainHome.showSnackBar(error.getMessage(), R.color.red_cinnabar);
                            }


                        }
                );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != meetingSubscription) {
            meetingSubscription.dispose();
        }
    }

}
