package com.meeting.management.api;

import com.meeting.management.model.MeetingResponse;
import com.meeting.management.model.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MeetingService {


    @GET("user/{userId}/upcoming-meetings")
    Observable<ResponseModel<MeetingResponse>> getmeetingInfo(@Path("userId") String userId);


}
