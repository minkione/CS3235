package com.masterlock.api.util;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit.RetrofitError;
import retrofit.RetrofitError.Kind;

public class ApiError extends RuntimeException {
    public static final String ACCOUNT_CREATION_INVALID_CODE = "100.3";
    public static final String CHANGED_ACCOUNT_SETTINGS = "403.3";
    public static final String CURRENT_PASSWORD_INCORRECT = "130.1";
    public static final String EMAIL_ALREADY_ASSOCIATED = "130.2";
    public static final String EMAIL_ALREADY_EXISTS = "117.9";
    public static final String EMAIL_ALREADY_IN_USE = "110.5";
    public static final String EMAIL_LIMIT_REACHED = "130.6";
    public static final String EMAIL_REQUIRED = "117.1";
    public static final String FORBIDDEN = "403.0";
    public static final String INVALID_ACTIVATION_CODE = "115.1";
    public static final String INVALID_API_KEY = "403.1";
    public static final String INVALID_EMAIL = "117.2";
    public static final String INVALID_EMAIL_VERIFICATION_CODE = "130.3";
    public static final String INVALID_ID = "100.6";
    public static final String INVALID_INVITATION_CODE = "118.23";
    public static final String INVALID_KMS_DEVICE_ID = "116.3";
    public static final String INVALID_MOBILE_NUMBER = "117.8";
    public static final String INVALID_MOBILE_PHONE = "110.47";
    public static final String INVALID_PERMISSION = "403.4";
    public static final String INVALID_USER_NAME = "110.4";
    public static final String MESSAGE_DELIMITER = "::";
    public static final String MOBILE_ALREADY_EXISTS = "117.10";
    public static final int NO_INTERNET_STATUS = -1;
    public static final String SESSION_EXPIRED = "403.2";
    public static final String SMS_LIMIT_REACHED = "130.5";
    public static final String USER_ALREADY_HAS_ACCESS = "118.5";
    public static final String USER_NAME_ALREADY_TAKEN = "130.4";
    String code;
    boolean handled = false;
    @SerializedName("Message")
    String message;
    @SerializedName("ModelState")
    HashMap<String, Object> modelState;
    int status;

    private ApiError() {
    }

    public static ApiError generateError(Throwable th) {
        ApiError apiError;
        if (th instanceof RetrofitError) {
            RetrofitError retrofitError = (RetrofitError) th;
            if (retrofitError.getResponse() == null || retrofitError.getKind() == Kind.NETWORK) {
                apiError = new ApiError();
                apiError.setStatus(-1);
                apiError.setMessage(retrofitError.getMessage());
            } else {
                try {
                    apiError = (ApiError) retrofitError.getBodyAs(ApiError.class);
                    apiError.status = retrofitError.getResponse().getStatus();
                    if (apiError.modelState != null) {
                        apiError.parseModelState();
                    } else {
                        apiError.parseMessage();
                    }
                } catch (Exception unused) {
                    apiError = new ApiError();
                    apiError.message = th.getMessage();
                }
            }
        } else if (th instanceof ApiError) {
            return (ApiError) th;
        } else {
            apiError = new ApiError();
            apiError.message = th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName();
        }
        return apiError;
    }

    public void parseMessage() {
        String[] split = getMessage().split(MESSAGE_DELIMITER);
        if (split.length == 2) {
            this.code = split[0];
            this.message = split[1];
        }
    }

    public String parseModelState() {
        String str = "";
        try {
            if (this.modelState == null || this.modelState.size() <= 0) {
                return str;
            }
            for (String str2 : this.modelState.keySet()) {
                this.message = (String) ((ArrayList) this.modelState.get(str2)).get(0);
                parseMessage();
            }
            return str;
        } catch (Exception unused) {
            return "";
        }
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public boolean isHandled() {
        return this.handled;
    }

    public void setHandled(boolean z) {
        this.handled = z;
    }
}
