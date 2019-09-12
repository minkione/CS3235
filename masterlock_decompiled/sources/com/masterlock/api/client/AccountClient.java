package com.masterlock.api.client;

import com.masterlock.api.entity.AuthRequest;
import com.masterlock.api.entity.AuthResponse;
import com.masterlock.api.entity.CreateAccountRequest;
import com.masterlock.api.entity.EmailPhoneVerificationRequest;
import com.masterlock.api.entity.EmailPhoneVerificationResponse;
import com.masterlock.api.entity.ForgotRequest;
import com.masterlock.api.entity.NotificationEventSettingsRequest;
import com.masterlock.api.entity.NotificationEventSettingsResponse;
import com.masterlock.api.entity.ProfileEmailVerificationRequest;
import com.masterlock.api.entity.ProfileMobilePhoneVerificationRequest;
import com.masterlock.core.AccountProfileInfo;
import java.util.List;
import java.util.Map;
import p009rx.Observable;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface AccountClient {
    @POST("/v4/account/authenticate")
    Observable<AuthResponse> authenticateCredentials(@Query("apikey") String str, @Body AuthRequest authRequest);

    @POST("/v5/account/validate/{id}/{validationType}/{code}")
    Response confirmAccountCreationCode(@Query("apikey") String str, @Path("id") String str2, @Path("validationType") String str3, @Path("code") String str4);

    @POST("/v5/account")
    AuthResponse createAccount(@Query("apikey") String str, @Body CreateAccountRequest createAccountRequest);

    @POST("/v5/account/emailverification")
    EmailPhoneVerificationResponse createEmailPhoneVerification(@Query("apikey") String str, @Body EmailPhoneVerificationRequest emailPhoneVerificationRequest);

    @POST("/v4/account/resetpassword")
    Response forgotPasscode(@Query("apikey") String str, @Body ForgotRequest forgotRequest);

    @POST("/v4/account/retrieveusername")
    Response forgotUsername(@Query("apikey") String str, @Body ForgotRequest forgotRequest);

    @GET("/v4/account/emailverification/{id}")
    Object getEmailVerificationDetails(@Query("apikey") String str, @Path("id") String str2);

    @GET("/v5/notifications")
    NotificationEventSettingsResponse getNotificationEventSettings(@QueryMap Map<String, String> map);

    @DELETE("/v5/account/profile/mobilephonenumber/{mobilePhoneNumber}")
    Response removeMobilePhoneNumber(@QueryMap Map<String, String> map, @Path("mobilePhoneNumber") String str);

    @PUT("/v5/notifications")
    Response updateNotificationEventSettings(@QueryMap Map<String, String> map, @Body List<NotificationEventSettingsRequest> list);

    @PUT("/v5/account/profile")
    Response updateProfileInfo(@QueryMap Map<String, String> map, @Body AccountProfileInfo accountProfileInfo);

    @POST("/v5/account/profile/emailverification")
    Response verifyProfileEmail(@QueryMap Map<String, String> map, @Body ProfileEmailVerificationRequest profileEmailVerificationRequest);

    @POST("/v5/account/profile/mobilephoneverification")
    Response verifyProfileMobilePhone(@QueryMap Map<String, String> map, @Body ProfileMobilePhoneVerificationRequest profileMobilePhoneVerificationRequest);
}
