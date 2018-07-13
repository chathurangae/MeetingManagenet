package com.meeting.management.api;

import com.meeting.management.model.AuthenticationResponse;
import com.meeting.management.model.ResponseModel;
import com.meeting.management.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("/user/sign-in")
    Observable<ResponseModel<AuthenticationResponse>> authenticate(@Body User authDetails);
}
