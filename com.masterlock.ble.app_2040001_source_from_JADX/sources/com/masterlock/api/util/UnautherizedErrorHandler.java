package com.masterlock.api.util;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class UnautherizedErrorHandler implements ErrorHandler {
    public Throwable handleError(RetrofitError retrofitError) {
        return null;
    }
}
