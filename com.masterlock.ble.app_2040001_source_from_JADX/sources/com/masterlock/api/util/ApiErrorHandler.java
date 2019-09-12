package com.masterlock.api.util;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class ApiErrorHandler implements ErrorHandler {
    private IResourceWrapper mResourceWrapper;

    public ApiErrorHandler(IResourceWrapper iResourceWrapper) {
        this.mResourceWrapper = iResourceWrapper;
    }

    public Throwable handleError(RetrofitError retrofitError) {
        ApiError generateError = ApiError.generateError(retrofitError);
        if (-1 == generateError.getStatus()) {
            generateError.setMessage(this.mResourceWrapper.stringFromName("no_connection_alert_body"));
            generateError.setHandled(false);
        } else if (ApiError.FORBIDDEN.equals(generateError.getCode()) || ApiError.INVALID_API_KEY.equals(generateError.getCode()) || ApiError.SESSION_EXPIRED.equals(generateError.getCode()) || ApiError.CHANGED_ACCOUNT_SETTINGS.equals(generateError.getCode()) || ApiError.INVALID_PERMISSION.equals(generateError.getCode())) {
            generateError.setHandled(true);
            this.mResourceWrapper.logOut();
        }
        return generateError;
    }
}
