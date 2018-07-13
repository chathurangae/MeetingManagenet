package com.meeting.management.api;

import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.meeting.management.model.ApiError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory wrappedFactory;

    private ApiCallAdapterFactory() {
        this.wrappedFactory = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new ApiCallAdapterFactory();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new ApiCallAdapter(retrofit, wrappedFactory.get(returnType, annotations, retrofit));
    }

    private static class ApiCallAdapter<R> implements CallAdapter<R, Object> {

        private final CallAdapter<?, ?> wrappedAdapter;
        private final Retrofit retrofit;

        public ApiCallAdapter(Retrofit retrofit, CallAdapter<R, ?> wrapped) {
            this.retrofit = retrofit;
            this.wrappedAdapter = wrapped;
        }

        @Override
        public Type responseType() {
            return this.wrappedAdapter.responseType();
        }

        @Override
        public Object adapt(Call call) {
            return ((Observable<R>) this.wrappedAdapter.adapt(call))
                    .onErrorResumeNext(throwable -> {
                        Throwable result = throwable;
                        if (throwable instanceof HttpException) {
                            HttpException httpException = (HttpException) throwable;
                            Response<?> response = httpException.response();
                            Converter<ResponseBody, ApiError> converter = retrofit.
                                    responseBodyConverter(ApiError.class, new Annotation[0]);
                            try {
                                result = converter.convert(Objects.requireNonNull(response.errorBody()));
                            } catch (Exception e) {
                                result = throwable;
                            }
                        }
                        return Observable.error(result);
                    });
        }
    }
}
